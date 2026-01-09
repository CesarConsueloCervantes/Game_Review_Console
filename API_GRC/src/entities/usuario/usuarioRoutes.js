const express = require('express');
const { body, param, query, check } = require('express-validator');
const validate = require('../../middlewares/validation');
const checkMongoId = require('../../middlewares/checkMongoId');
const Game = require('../game/gameModel');
const usuarioController = require('./usuarioController');

const router = express.Router();

router.get('/usuarios', usuarioController.getUsuarios);

router.get('/usuarios/:id',
    param('id').isMongoId().withMessage('Invalid usuario ID'),
    validate,
    usuarioController.getUsuario
);

router.post('/usuarios',
    body('nombre').isString().notEmpty().withMessage('Nombre is required and must be a string'),
    body('password').isLength({ min: 1 }).withMessage('Password must be at least 1 characters long'),
    validate,
    usuarioController.createUsuario
);

router.put('/usuarios/:id',
    body('games_followed').isArray().withMessage('Must be an array'),
    body('games_followed.*')
    .isMongoId().withMessage('Each item must be a valid MongoId')
    .custom(async (id) => {
        const exists = await Game.exists({ _id: id });
        if (!exists) throw new Error(`Game not found: ${id}`);
    }),
    body('nombre').optional().isString().withMessage('Nombre must be a string'),
    body('password').optional().isLength({ min: 1 }).withMessage('Password must be at least 1 characters long'),
    validate,
    usuarioController.putUsuario
);

router.delete('/usuarios/:id',
    param('id').isMongoId().withMessage('Invalid review ID'),
    validate,
    usuarioController.deleteUsuario
);

router.post('/usuarios/subscription/:id',
    param('id').isMongoId().withMessage('Invalid usuario ID'),
    checkMongoId('game', Game),
    validate,
    usuarioController.subscription
);

module.exports = router;