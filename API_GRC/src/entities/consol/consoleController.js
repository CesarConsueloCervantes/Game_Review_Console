const Console = require("./consoleModel");
const GameVercion = require("../game/gameVercionModel");

exports.getConsoles = async (req, res, next) => {
  try {
    const consoles = await Console.find();
    res.status(200).send(consoles);
  } catch (error) {
    next(error);
  }
};

exports.createConsole = async (req, res, next) => {
  try {
    const newConsole = new Console(req.body);
    await newConsole.save();
    res.status(201).send(newConsole);
  } catch (error) {
    next(error);
  }
};

exports.deleteConsole = async (req, res, next) => {
  try {
    const console = await Console.findByIdAndDelete(req.params.id);
    if (!console) {
      res.status(404).json({ message: 'Console no encontrado' });
    } else {
      res.status(204).send();
    }
  } catch (error) {
    next(error);
  }
};

exports.getGamesByConsole = async (req, res, next) => {
  try{
    const gamevercions = await GameVercion.find({ console: req.params.id });
    res.status(200).send(gamevercions);
  }catch(error){
    next(error);
  }
};
