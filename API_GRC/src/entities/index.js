const express = require('express');

const router = express.Router();

const consoleRoutes = require('./consol/consoleRoutes');
const gameRoutes = require('./game/gameRoutes');
const reviewRoutes = require('./review/reviewRoutes');
const usuarioRoutes = require('./usuario/usuarioRoutes');
const authRoutes = require('./auth/authRoutes');

router.use(consoleRoutes);
router.use(gameRoutes);
router.use(reviewRoutes);
router.use(usuarioRoutes);
router.use(authRoutes);

module.exports = router;