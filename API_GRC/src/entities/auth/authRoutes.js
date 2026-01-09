const express = require('express');
const { body, param, query } = require('express-validator');
const validate = require('../../middlewares/validation');
const authController = require('./authController');

const router = express.Router();

router.post('/auth/register',
  body('nombre').isString().notEmpty().withMessage('Nombre is required and must be a string'),
  body('password').isLength({ min: 1 }).withMessage('Password must be at least 1 characters long'),
  validate,
  authController.register
);

router.post('/auth/login',
  body('nombre').isString().notEmpty().withMessage('Nombre is required and must be a string'),
  body('password').isLength({ min: 1 }).withMessage('Password must be at least 1 characters long'),
  validate,
  authController.login
);

module.exports = router;