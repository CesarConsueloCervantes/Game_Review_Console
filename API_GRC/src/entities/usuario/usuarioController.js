const Usuario = require('./usuarioModel');
const Review = require('../review/reviewModel');
const Game = require('../game/gameModel');

exports.getUsuarios = async (req, res, next) => {
    try {
        const usuarios = await Usuario.find();
        res.status(200).send(usuarios);
    } catch (error) {
        next(error);
    }
};

exports.getUsuario = async (req, res, next) => {
    try {
        const usuario = await Usuario.findById(req.params.id).lean();
        if (!usuario) {
            return res.status(404).json({ message: 'Usuario not found' });
        }
        const reviews = await Review.find({ usuario: usuario._id }).populate('game_version', 'game_name console_name').lean(); 
        const games_followed_names = await Promise.all(
            usuario.games_followed.map(async (gameId) => {
                const game = await Game.findById(gameId).lean();
                return game?.game_title ?? 'Unknown Game';
            })
        );
        reviews.forEach(review => { review.usuario = null; });
        usuario.games_followed_names = games_followed_names;
        usuario.reviews = reviews;
        usuario.password = null;
        res.status(200).send(usuario);
    } catch (error) {
        next(error);
    }
};

exports.createUsuario = async (req, res, next) => {
    try {
        const newUsuario = new Usuario(req.body);
        if (req.body.games_followed != null) {
            return res.status(400).json({ message: 'New usuario cannot follow games upon creation' });
        }
        await newUsuario.save();
        res.status(201).send(newUsuario);
    } catch (error) {
        next(error);
    }
};

exports.putUsuario = async (req, res, next) => {
    try {
        const updatedUsuario = await Usuario.findByIdAndUpdate(req.params.id, req.body, { new: true });
        if (!updatedUsuario) {
            return res.status(404).json({ message: 'Usuario not found' });
        }
        res.status(200).send(updatedUsuario);
    } catch (error) {
        next(error);
    }
};

exports.deleteUsuario = async (req, res, next) => {
    try{
        const deleteUsuario = await Usuario.findById(req.params.id);
        if (!deleteUsuario) {
            return res.status(404).json({ message: 'Usuario not found' });
        }
        deleteUsuario.nombre = 'user alowed';
        deleteUsuario.password = 'deleted_' + Date.now();
        deleteUsuario.image = '';
        deleteUsuario.games_followed = [];
        await deleteUsuario.save();
        res.status(200).send({ message: 'Usuario deleted successfully' });
    } catch (error) {
        next(error);
    }
};

exports.subscription = async (req, res, next) => {
    try {
        const usuario = await Usuario.findById(req.params.id);
        if (!usuario) {
            return res.status(404).json({ message: 'Usuario not found' });
        }
        usuario.games_followed.push(req.body.game);
        await usuario.save();
        res.status(200).send(usuario);
    }catch (error) {
        next(error);
    }
};