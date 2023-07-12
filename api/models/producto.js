const mysql = require('mysql2');
const DB = require('../DB.JS');
const db = new DB();

class Producto {
    constructor() {
      this.connection = db.connection; // Conexión a la base de datos
    }
  
    getAllProductos(callback) {
      const query = `
        SELECT Producto.id_producto, Producto.id_categoria, Producto.nombre, Producto.descripcion, Producto.precio, Categoria.categoriaPrincipal
        FROM Producto
        INNER JOIN Categoria ON Producto.id_categoria = Categoria.id_categoria
      `;
  
      this.connection.query(query, (err, results) => {
        if (err) {
          console.error('Error al obtener los productos:', err);
          callback(err, null);
          return;
        }
        callback(null, results);
      });
    }
  
    getProductoById(idProducto, callback) {
      const query = `
        SELECT producto.id_producto, producto.id_categoria, producto.nombre, producto.descripcion, producto.precio, categorias.nombre_categoria
        FROM producto
        INNER JOIN categorias ON producto.id_categoria = categorias.id_categoria
        WHERE producto.id_producto = ?
      `;
  
      this.connection.query(query, [idProducto], (err, results) => {
        if (err) {
          console.error('Error al obtener el producto:', err);
          callback(err, null);
          return;
        }
  
        if (results.length === 0) {
          callback({ error: 'Producto no encontrado' }, null);
          return;
        }
  
        callback(null, results[0]);
      });
    }
  
    getProductosByCategoryId(categoryId, callback) {
      const query = `
        SELECT producto.id_producto, producto.id_categoria, producto.nombre, producto.descripcion, producto.precio, categorias.nombre_categoria
        FROM producto
        INNER JOIN categorias ON producto.id_categoria = categorias.id_categoria
        WHERE producto.id_categoria = ?
      `;
  
      this.connection.query(query, [categoryId], (err, results) => {
        if (err) {
          console.error('Error al obtener los productos por ID de categoría:', err);
          callback(err, null);
          return;
        }
        callback(null, results);
      });
    }
  
    insertProducto(idCategoria, nombre, descripcion, precio, callback) {
      const query = `
        INSERT INTO producto (id_categoria, nombre, descripcion, precio)
        VALUES (?, ?, ?, ?)
      `;
      const values = [idCategoria, nombre, descripcion, precio];
  
      this.connection.query(query, values, (err, result) => {
        if (err) {
          console.error('Error al insertar el producto:', err);
          callback(err, null);
          return;
        }
        callback(null, result);
      });
    }
  
    updateProducto(idProducto, idCategoria, nombre, descripcion, precio, callback) {
      const query = `
        UPDATE producto
        SET id_categoria = ?, nombre = ?, descripcion = ?, precio = ?
        WHERE id_producto = ?
      `;
      const values = [idCategoria, nombre, descripcion, precio, idProducto];
  
      this.connection.query(query, values, (err, result) => {
        if (err) {
          console.error('Error al modificar el producto:', err);
          callback(err, null);
          return;
        }
        callback(null, result);
      });
    }
  
    deleteProducto(idProducto, callback) {
      const query = 'DELETE FROM producto WHERE id_producto = ?';
  
      this.connection.query(query, [idProducto], (err, result) => {
        if (err) {
          console.error('Error al eliminar el producto:', err);
          callback(err, null);
          return;
        }
        callback(null, result);
      });
    }
  }
  
  module.exports = Producto;