const Review = require('./reviewModel');
const GameVercion = require('../game/gameVercionModel');
const calculateScore = require('../../utils/calculateScore');

exports.createReview = async (req, res, next) => {
    try {
        const newReview = new Review(req.body);
        await newReview.save();
        if (req.body.review === 'recommended'){
            await GameVercion.findByIdAndUpdate(req.body.game_version, { $inc: { review_count_recomendated: 1 } }, { new: true });
        } if (req.body.review === 'mixed'){
            await GameVercion.findByIdAndUpdate(req.body.game_version, { $inc: { review_count_mixed: 1 } }, { new: true });
        } else if (req.body.review === 'not_recommended'){
            await GameVercion.findByIdAndUpdate(req.body.game_version, { $inc: { review_count_not_recomendated: 1 } }, { new: true });
        }
        res.status(201).send(newReview);
    } catch (error) {
        next(error);
    }
};

exports.updateReview = async (req, res, next) => {
    try {
        // Obtener la review anterior para saber qué contador ajustar
        const oldReview = await Review.findById(req.params.id);
        if (!oldReview) {
            return res.status(404).json({ message: 'Review not found' });
        }

        // Actualizar la review
        const updatedReview = await Review.findByIdAndUpdate(req.params.id, req.body, { new: true });

        // Si no hubo cambios relevantes, responder con el documento actualizado
        if (!updatedReview) {
            return res.status(404).json({ message: 'Review not found after update' });
        }

        // Mapeo de tipo de review a campos de contador en GameVercion
        const counterFieldFromType = (type) => {
            if (type === 'recommended') return 'review_count_recomendated';
            if (type === 'mixed') return 'review_count_mixed';
            if (type === 'not_recommended') return 'review_count_not_recomendated';
            return null;
        };

        const oldType = oldReview.review;
        const newType = updatedReview.review;
        const oldGameId = oldReview.game_version ? oldReview.game_version.toString() : null;
        const newGameId = updatedReview.game_version ? updatedReview.game_version.toString() : null;

        // Si cambió el tipo o la versión del juego, ajustar contadores
        if (oldType !== newType || oldGameId !== newGameId) {
            // Decrementar en la versión antigua (si aplica)
            if (oldGameId) {
                const oldField = counterFieldFromType(oldType);
                if (oldField) {
                    await GameVercion.findByIdAndUpdate(oldGameId, { $inc: { [oldField]: -1 } },{ new: true });
                }
            }

            // Incrementar en la versión nueva (si aplica)
            if (newGameId) {
                const newField = counterFieldFromType(newType);
                if (newField) {
                    await GameVercion.findByIdAndUpdate(newGameId, { $inc: { [newField]: 1 } },{ new: true });
                }
            }

            // Recalcular porcentaje y actualizar ambas versiones afectadas
            const recalcAndUpdate = async (gameId) => {
                if (!gameId) return;
                const gv = await GameVercion.findById(gameId).lean();
                if (!gv) return;

                const positive = gv.review_count_recomendated || 0;
                const negative = gv.review_count_not_recomendated || 0;
                const mixed = gv.review_count_mixed || 0;

                const score = calculateScore(positive, negative, mixed);
                const percent = Math.round(score * 100);

                // Al actualizar review_percent, el pre('findOneAndUpdate') en el modelo recalculará review_rate
                await GameVercion.findByIdAndUpdate(gameId, { review_percent: percent });
            };

            await Promise.all([recalcAndUpdate(oldGameId), recalcAndUpdate(newGameId)]);
        }

        return res.status(200).send(updatedReview);
        
    } catch (error) {
        next(error);
    }
};

exports.deleteReview = async (req, res, next) => {
    try{
        const review = await Review.findByIdAndDelete(req.params.id);
        if (!review) {
            res.status(404).json({ message: 'Review not found' });
        } else {
            await GameVercion.findByIdAndUpdate(review.game_version, { $inc: {
                review_count_recomendated: review.review === 'recommended' ? -1 : 0,
                review_count_mixed: review.review === 'mixed' ? -1 : 0,
                review_count_not_recomendated: review.review === 'not_recommended' ? -1 : 0
            }},{ new: true });
            res.status(204).send();
        }
    }catch (error){
        next(error);
    }
};