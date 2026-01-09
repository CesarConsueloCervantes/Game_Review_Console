const express = require('express');
const { body, param, query, check } = require('express-validator');
const validate = require('../../middlewares/validation');
const checkMongoId = require('../../middlewares/checkMongoId');
const GameVercion = require('../game/gameVercionModel');
const reviewController = require('./reviewController');

const router = express.Router();

router.post('/review',
    checkMongoId('game_version', GameVercion),
    body('review').isIn(['recommended', 'mixed', 'not_recommended']).withMessage('review must be one of recommended, mixed, not_recommended'),
    validate,
    reviewController.createReview
);

router.put('/review/:id',
    param('id').isMongoId().withMessage('Invalid review ID'),
    checkMongoId('game_version', GameVercion),  
    body('review').optional().isIn(['recommended', 'mixed', 'not_recommended']).withMessage('review must be one of recommended, mixed, not_recommended'),
    validate,
    reviewController.updateReview
);

router.delete('/review/:id',
    param('id').isMongoId().withMessage('Invalid review ID'),
    validate,
    reviewController.deleteReview
);

module.exports = router;