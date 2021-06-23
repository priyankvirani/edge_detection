import 'dart:async';
import 'dart:io';
import 'dart:typed_data';
import 'package:edge_detection/edge_detection.dart';
import 'package:edge_detection/edge_detection.dart';
import 'package:edge_detection/factory/pathfrom.dart';
import 'package:flutter/material.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/services.dart';
import 'package:image_picker/image_picker.dart';


// void main() {
//   runApp(MyApp());
// }
//
// class MyApp extends StatefulWidget {
//   @override
//   _MyAppState createState() => _MyAppState();
// }
//
// class _MyAppState extends State<MyApp> {
//   String? _imagePath;
//
//   @override
//   void initState() {
//     super.initState();
//   }
//
//   Future<void> getImage() async {
//     String? imagePath;
//     // Platform messages may fail, so we use a try/catch PlatformException.
//     // We also handle the message potentially returning null.
//     try {
//       imagePath = (await EdgeDetection.detectEdge);
//     } on PlatformException {
//       imagePath = 'Failed to get cropped image path.';
//     }
//
//     // If the widget was removed from the tree while the asynchronous platform
//     // message was in flight, we want to discard the reply rather than calling
//     // setState to update our non-existent appearance.
//     if (!mounted) return;
//
//     setState(() {
//       _imagePath = imagePath;
//     });
//
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return MaterialApp(
//       home: Scaffold(
//         appBar: AppBar(
//           title: const Text('Plugin example app'),
//         ),
//         body: Column(
//           mainAxisAlignment: MainAxisAlignment.center,
//           crossAxisAlignment: CrossAxisAlignment.center,
//           children: [
//             Center(
//               child: ElevatedButton(
//                 onPressed: getImage,
//                 child: Text('Scan'),
//               ),
//             ),
//             SizedBox(height: 20),
//             Text('Cropped image path:'),
//             Padding(
//               padding: const EdgeInsets.only(top: 0, left: 0, right: 0),
//               child: Text(
//                 '$_imagePath\n',
//                 style: TextStyle(fontSize: 10),
//               ),
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: MyHomePage(title: 'opencv_4 Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, this.title}) : super(key: key);

  final String? title;

  @override
  _MyHomePageState createState() => _MyHomePageState();

}

class _MyHomePageState extends State<MyHomePage> {

  File? _image;
  Uint8List? _byte, salida;
  String _versionOpenCV = 'OpenCV';
  bool _visible = false;
  //uncomment when image_picker is installed
  final picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _getOpenCVVersion();
  }

  testOpenCV({
    required String pathString,
    required CVPathFrom pathFrom,
    required double thresholdValue,
    required double maxThresholdValue,
    required int thresholdType,
  }) async {
    try {
      //test with threshold
      _byte = await EdgeDetection.threshold(
        pathFrom: pathFrom,
        pathString: pathString,
        maxThresholdValue: maxThresholdValue,
        thresholdType: thresholdType,
        thresholdValue: thresholdValue,
      );

      setState(() {
        _byte;
        _visible = false;
      });
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  /// metodo que devuelve la version de OpenCV utilizada
  Future<void> _getOpenCVVersion() async {
    String? versionOpenCV = await EdgeDetection.version();

    setState(() {
      _versionOpenCV = 'OpenCV: ' + versionOpenCV!;
      print('$versionOpenCV');
    });
  }

  _testFromAssets() async {

    testOpenCV(
      pathFrom: CVPathFrom.ASSETS,
      pathString: 'assets/Test.JPG',
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: EdgeDetection.THRESH_BINARY,
    );
    setState(() {
      _visible = true;
    });

  }

  _testFromUrl() async {
    testOpenCV(
      pathFrom: CVPathFrom.URL,
      pathString:
      'https://mir-s3-cdn-cf.behance.net/project_modules/max_1200/16fe9f114930481.6044f05fca574.jpeg',
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: EdgeDetection.THRESH_BINARY,
    );
    setState(() {
      _visible = true;
    });
  }

  _testFromCamera() async {

    //uncomment when image_picker is installed
    final pickedFile = await picker.getImage(source: ImageSource.camera);
    _image = File(pickedFile!.path);
    testOpenCV(
      pathFrom: CVPathFrom.GALLERY_CAMERA,
      pathString: _image!.path,
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: EdgeDetection.THRESH_BINARY,
    );

    setState(() {
      _visible = true;
    });
  }

  _testFromGallery() async {
    //uncomment when image_picker is installed
    final pickedFile = await picker.getImage(source: ImageSource.gallery);
    _image = File(pickedFile!.path);
    testOpenCV(
      pathFrom: CVPathFrom.GALLERY_CAMERA,
      pathString: _image!.path,
      thresholdValue: 150,
      maxThresholdValue: 200,
      thresholdType: EdgeDetection.THRESH_BINARY,
    );

    setState(() {
      _visible = true;
    });

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title!),
      ),
      body: SingleChildScrollView(
        child: Container(
          child: Column(
            children: [
              Container(
                  margin: EdgeInsets.only(top: 20),
                  child: Center(
                    child: Column(
                      children: <Widget>[
                        Text(
                          _versionOpenCV,
                          style: TextStyle(fontSize: 23),
                        ),
                        Container(
                          margin: EdgeInsets.only(top: 5),
                          child: _byte != null
                              ? Image.memory(
                            _byte!,
                            width: 300,
                            height: 300,
                            fit: BoxFit.fill,
                          )
                              : Container(
                            width: 300,
                            height: 300,
                            child: Icon(
                              Icons.camera_alt,
                              color: Colors.grey[800],
                            ),
                          ),
                        ),
                        Visibility(
                            maintainAnimation: true,
                            maintainState: true,
                            visible: _visible,
                            child:
                            Container(child: CircularProgressIndicator())),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('test assets'),
                            onPressed: _testFromAssets,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('test url'),
                            onPressed: _testFromUrl,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('test gallery'),
                            onPressed: _testFromGallery,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                        SizedBox(
                          width: MediaQuery.of(context).size.width - 40,
                          child: TextButton(
                            child: Text('test camara'),
                            onPressed: _testFromCamera,
                            style: TextButton.styleFrom(
                              primary: Colors.white,
                              backgroundColor: Colors.teal,
                              onSurface: Colors.grey,
                            ),
                          ),
                        ),
                      ],
                    ),
                  )),
            ],
          ),
        ),
      ),
    );
  }

}