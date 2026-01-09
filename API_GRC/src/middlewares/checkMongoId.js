const mongoose = require('mongoose');

/**
 * Middleware que valida un ObjectId contra un modelo
 * @param {string} paramName - Nombre del campo a validar (ej: 'userId', 'productId')
 * @param {mongoose.Model} Model - Modelo de Mongoose
 */
function checkMongoId(paramName, Model) {
    return async (req, res, next) => {
        try {
            const id = req.body[paramName] || req.params[paramName] || req.query[paramName];

            if (!id) {
                return res.status(400).json({ error: `parameter ${paramName} is missing` });
            }

            if (!mongoose.Types.ObjectId.isValid(id)) {
                return res.status(400).json({ error: 'ID not valid' });
            }

            const exists = await Model.exists({ _id: id });
            if (!exists) {
                return res.status(404).json({ error: `${paramName} not exist` });
            }

            next();
        } catch (err) {
            next(err);
        }
    };
}

module.exports = checkMongoId;