const express = require('express');
const cors = require('cors');
const app = express();
const port = 3000;

const DB = require('./DB.JS');
const db = new DB();

db.connect();

// Resto del código de tu API

app.use(cors());
app.use(express.json());

const productoRoutes = require('./router/productoRouter');
app.use(productoRoutes);

const categoriaRoutes = require('./router/categoriaRouter');
app.use(categoriaRoutes);
// ...

app.listen(port, () => {
  console.log(`Servidor corriendo en http://localhost:${port}`);
});

app.get('/', (req, res) => {
    res.send('¡Hola, mundo!');
  });
  