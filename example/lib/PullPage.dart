import 'package:flutter/material.dart';
import 'package:flutter_hw_live/flutter_hw_live.dart';

class PullPage extends StatefulWidget {
  @override
  _PullPageState createState() => _PullPageState();
}

class _PullPageState extends State<PullPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: new Stack(
        children: <Widget>[
          HwPullView(
            onHwPullViewCreatedCallback: (controller) {
              debugPrint("播放器已创建");
              //controller.start("https://www.w3school.com.cn/example/html5/mov_bbb.mp4", false);
              controller.start("rtmp://subang.7oks.com/live/123", true);

            },
          )
        ],
      ),
    );
  }
}
