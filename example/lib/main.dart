import 'package:flutter/material.dart';

import 'PullPage.dart';
import 'PushPage.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    //initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  // Future<void> initPlatformState() async {
  //   String platformVersion;
  //   // Platform messages may fail, so we use a try/catch PlatformException.
  //   try {
  //     platformVersion = await HwLivePlugin.platformVersion;
  //   } on PlatformException {
  //     platformVersion = 'Failed to get platform version.';
  //   }
  //
  //   // If the widget was removed from the tree while the asynchronous platform
  //   // message was in flight, we want to discard the reply rather than calling
  //   // setState to update our non-existent appearance.
  //   if (!mounted) return;
  //
  //   setState(() {
  //     _platformVersion = platformVersion;
  //   });
  // }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '华为直播demo',
      home: HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('华为直播demo'),
      ),
      body: new Column(
        children: <Widget>[
          new FlatButton(
              onPressed: () {
                Navigator.push(
                  context,
                  new MaterialPageRoute(builder: (context) {
                    return PushPage();
                  }),
                );
              },
              child: new Text("推流")),
          SizedBox(
            height: 30,
          ),
          new FlatButton(
              onPressed: () {
                Navigator.push(
                  context,
                  new MaterialPageRoute(builder: (context) {
                    return PullPage();
                  }),
                );
              },
              child: new Text("拉流")),
        ],
      ),
    );
  }
}
