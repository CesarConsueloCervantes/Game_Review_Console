const express = require('express');
const { body, param, query, check } = require('express-validator');
const validate = require('../../middlewares/validation');
const checkMongoId = require('../../middlewares/checkMongoId');
const Game = require('./gameModel');
const Console = require('../consol/consoleModel');
const gameController = require('./gameController');

const router = express.Router();

router.get('/game', validate, gameController.getGames);
router.get('/features', validate, gameController.getGamesFeatured)
router.post('/game',
    body('game_title').isString().withMessage('game_title must be a string').notEmpty().withMessage('game_title is required'),
    validate,
    gameController.createGame
);
router.get('/search/:search',
    param('search').isString().withMessage('search must be a string').notEmpty().withMessage('search is required'),
    validate,
    gameController.getGamesSearch
);
router.delete('/game/:id',
    param('id').isMongoId().withMessage('Invalid game ID'),
    validate,
    gameController.deleteGame
);

router.get('/game/:id/version',
    param('id').isMongoId().withMessage('Invalid game ID'),
    validate,
    gameController.getGameVercions
);

router.get('/game/version/:id',
    param('id').isMongoId().withMessage('Invalid game vercion ID'),
    validate,
    gameController.getVercion
);

router.post('/game/version',
    checkMongoId('game', Game),
    checkMongoId('console', Console),
    body('image_vercion').optional().isString().withMessage('image_vercion must be a string'),
    validate,
    gameController.createGameVercion
);

router.delete('/game/version/:id',
    param('id').isMongoId().withMessage('Invalid game vercion ID'),
    validate,
    gameController.deleteGameVercion
);

router.get('/game/version/:id/reviews',
    param('id').isMongoId().withMessage('Invalid game version ID'),
    validate,
    gameController.getReviewsByGameVercion
);

module.exports = router;