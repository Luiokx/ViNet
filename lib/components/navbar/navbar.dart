import 'package:flutter/material.dart';

class NavBar extends StatefulWidget {
  const NavBar({Key? key}) : super(key: key);

  @override
  State<NavBar> createState() {
    return _NavBarState();
  }
}

class _NavBarState extends State<NavBar> {
  List<bool> isSelected = [false, false, false];
  // TODO Datos mockeados, cambiarlos en algun momento
  List<String> categories = [
    'Acción',
    'Comedia',
    'Drama',
    'Aventura',
    'Ciencia Ficción',
    'Romance',
    'Suspenso',
    'Terror',
  ];

  void onItemTapped(int index) {
    setState(() {
      if (index != 3) {
        isSelected[index] = !isSelected[index];
        if (index == 2 && isSelected[index]) {
          _showCategoriesModal();
        }
      } else {
        _showCategoriesModal();
      }
    });
  }

  void _showCategoriesModal() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (BuildContext context) {
        return Container(
          height: MediaQuery.of(context).size.height,
          color:
              Colors.black.withOpacity(0.5), // Fondo oscuro semi-transparente
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Align(
                alignment: Alignment.topRight,
                child: GestureDetector(
                  onTap: () {
                    Navigator.pop(context); // Cierra la pantalla de categorías
                  },
                  child: const Padding(
                    padding: EdgeInsets.all(16.0),
                    child: Icon(Icons.close, color: Colors.white),
                  ),
                ),
              ),
              Container(
                width: MediaQuery.of(context).size.width * 0.8, // Ancho del 80%
                height:
                    MediaQuery.of(context).size.height * 0.6, // Alto del 60%
                decoration: BoxDecoration(
                  color: Colors.transparent, // Fondo transparente
                  borderRadius: BorderRadius.circular(16.0),
                ),
                child: ListView.builder(
                  itemCount: categories.length,
                  itemBuilder: (BuildContext context, int index) {
                    return ListTile(
                      title: Text(categories[index],
                          style: TextStyle(color: Colors.white)),
                      onTap: () {
                        // Agregar funcionalidad al seleccionar una categoría
                        Navigator.pop(context);
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget buildNavItem(int index, String title) {
    return GestureDetector(
      onTap: () => onItemTapped(index),
      child: Container(
          margin: EdgeInsets.only(
              left: index == 0
                  ? MediaQuery.of(context).size.width * 0.05
                  : MediaQuery.of(context).size.width * 0.01),
          width: index == 0 ? 85.0 : 100.0, // Fijar el ancho del botón
          height: 40.0, // Fijar el alto del botón
          padding: const EdgeInsets.all(8.0),
          decoration: BoxDecoration(
            border: Border.all(
                color: isSelected[index] ? Colors.white : Colors.transparent),
            borderRadius: BorderRadius.circular(30.0),
          ),
          child: Column(mainAxisAlignment: MainAxisAlignment.center, children: [
            Text(
              title,
              style: TextStyle(
                  color: isSelected[index] ? Colors.white : Colors.white,
                  fontSize: 16),
            )
          ])),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(15.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        children: <Widget>[
          Container(
            margin:
                EdgeInsets.only(left: MediaQuery.of(context).size.width * 0.03),
            child: const Text(
              'ViNet', // TODO cambiarlo por algun literal
              style: TextStyle(color: Colors.white, fontSize: 16),
            ),
          ),
          //TODO Hacer multilenguaje y cambiarlo
          buildNavItem(0, 'Series'),
          buildNavItem(1, 'Películas'),
          buildNavItem(2, 'Categorías'),
        ],
      ),
    );
  }
}
