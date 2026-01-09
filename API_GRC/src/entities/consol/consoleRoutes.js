const express = require('express');
const { body, param, query } = require('express-validator');
const validate = require('../../middlewares/validation');
const consoleController = require('./consoleController');

const router = express.Router();

router.get('/console', validate, consoleController.getConsoles);

router.post('/console',
  body('console_name').isString().withMessage('console_name must be a string').notEmpty().withMessage('console_name is required'),
  validate,
  consoleController.createConsole
);

router.delete('/console/:id',
  param('id').isMongoId().withMessage('Invalid console ID'),
  validate,
  consoleController.deleteConsole
);

router.get('/console/:id/games',
  param('id').isMongoId().withMessage('Invalid console ID'),
  validate,
  consoleController.getGamesByConsole
);

module.exports = router;