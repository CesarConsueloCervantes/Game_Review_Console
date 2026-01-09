const Usuario = require("../usuario/usuarioModel");


exports.register = async (req, res, next) => {
  try {
    const body = {
        nombre: req.body.nombre,
        password: req.body.password,
        games_followed: []
    };

    const existingUser = await Usuario.findOne({ nombre: body.nombre });
    if (existingUser) {
        return res.status(400).json({ message: 'El nombre de usuario ya está en uso' });
    }
    const newUsuario = new Usuario(body);
    await newUsuario.save();
    res.status(201).send(newUsuario);
  } catch (error) {
    next(error);
  }
};

exports.login = async (req, res, next) => {
    try {
        const { nombre, password } = req.body;
        const usuario = await Usuario.findOne({ nombre: nombre });
        if (!usuario || usuario.password !== password) {
            return res.status(401).json({ message: 'Nombre de usuario o contraseña incorrectos' });
        }
        usuario.password = undefined;
        res.status(200).send(usuario);
    }catch (error) {
        next(error);
    }
};