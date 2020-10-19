import 'package:flutter/material.dart';
import 'package:flutter_hw_live/flutter_hw_live.dart';

class PushPage extends StatefulWidget {
  @override
  _PushPageState createState() => _PushPageState();
}

class _PushPageState extends State<PushPage> {
  HwPushViewController mHWPushViewController;

  @override
  void initState() {
    FlutterPluginHwLive.instance.init("11", 1);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("推流"),
      ),
      body: Stack(
        children: <Widget>[
          HwPushView(
            onHwPushViewCreatedCallback: (controller) {
              debugPrint("视图已创建");
              mHWPushViewController = controller;
              //subang.7oks.com
              mHWPushViewController.startPublish("rtmp://subang.7oks.com/live/slh");
              // Future.delayed(Duration(seconds: 1), () {
              //   mHWPushViewController.startPublish("rtmp://kitpush.hwcloudlive.com/live1/sdkdemo1");
              //   // mHWPushViewController.startPublish("rtmp://subang.7oks.com/live/slh");
              // });
            },
            // onPushEventListener: (args) {
            //   debugPrint("onPushEventListener $args");
            // },
          ),
          new Column(
            children: <Widget>[
              Container(
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(50),
                  color: Colors.white,
                ),
                margin: EdgeInsets.symmetric(vertical: 5),
                child: new IconButton(
                    icon: Icon(Icons.switch_camera),
                    onPressed: () {
                      mHWPushViewController?.cameraSwitch();
                    }),
              ),
              // Container(
              //   decoration: BoxDecoration(
              //     borderRadius: BorderRadius.circular(50),
              //     color: Colors.white,
              //   ),
              //   margin: EdgeInsets.symmetric(vertical: 5),
              //   child: new IconButton(icon: Icon(Icons.beach_access), onPressed: () {
              //     mHWPushViewController?.setCameraZoom();
              //   }),
              // ),
              Container(
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(50),
                  color: Colors.white,
                ),
                margin: EdgeInsets.symmetric(vertical: 5),
                child: new IconButton(
                    icon: Icon(Icons.flash_auto),
                    onPressed: () {
                      mHWPushViewController?.flashLightSwitch();
                    }),
              ),

              Container(
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(50),
                  color: Colors.white,
                ),
                margin: EdgeInsets.symmetric(vertical: 5),
                child: new IconButton(
                    icon: Icon(Icons.beach_access),
                    onPressed: () {
                      mHWPushViewController?.beautySwitch();
                    }),
              ),
            ],
          )
        ],
      ),
    );
  }
}
