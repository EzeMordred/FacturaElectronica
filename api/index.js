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

const archivoRoutes = require('./router/productoRouter');
app.use(archivoRoutes);
// ...

app.listen(port, () => {
  console.log(`Servidor corriendo en http://localhost:${port}`);
});

app.get('/', (req, res) => {
    res.send('¡Hola, mundo!');
  });
  