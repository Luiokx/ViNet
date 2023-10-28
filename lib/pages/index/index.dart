import 'package:flutter/material.dart';
import 'package:vinet/components/navbar/navbar.dart';

class IndexPage extends StatelessWidget {
  const IndexPage({super.key});

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      backgroundColor: Colors.black,
      body: Column(
        children: <Widget>[NavBar()],
      ),
    );
  }
}
