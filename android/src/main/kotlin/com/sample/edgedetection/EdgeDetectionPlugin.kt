package com.sample.edgedetection

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import androidx.annotation.NonNull
import com.sample.edgedetection.factory.colormaps.ApplyColorMapFactory
import com.sample.edgedetection.factory.colorspace.CvtColorFactory
import com.sample.edgedetection.factory.imagefilter.*
import com.sample.edgedetection.factory.miscellaneous.AdaptiveThresholdFactory
import com.sample.edgedetection.factory.miscellaneous.DistanceTransformFactory
import com.sample.edgedetection.factory.miscellaneous.ThresholdFactory

import org.opencv.android.OpenCVLoader
import org.opencv.core.Core

class EdgeDetectionPlugin(
    private val registrar: Registrar,
    private val delegate: EdgeDetectionDelegate
) : MethodCallHandler {

    var OpenCVFLag = false

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {
            if (registrar.activity() != null) {
                val channel = MethodChannel(registrar.messenger(), "edge_detection")

                val delegate = EdgeDetectionDelegate(registrar.activity())

                registrar.addActivityResultListener(delegate)

                channel.setMethodCallHandler(EdgeDetectionPlugin(registrar, delegate))
            }
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result): Unit{

        if (!OpenCVFLag) {
            if (!OpenCVLoader.initDebug()) {
                println("Error on load OpenCV")
            } else {
                OpenCVFLag = true;
            }
        }

        if (registrar.activity() == null) {
            result.error(
                "no_activity",
                "edge_detection plugin requires a foreground activity.",
                null
            )
            return
        } else if (call.method.equals("edge_detect")) {
            delegate.openCameraActivity(call, result)
        } else {
            when (call.method) {

                "getVersion" -> {
                    try {
                        result.success(Core.VERSION)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }

                //Module: Image Filtering
                "bilateralFilter" -> {
                    try {
                        BilateralFilterFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("diameter") as Int,
                                call.argument<Int>("sigmaColor") as Int,
                                call.argument<Int>("sigmaSpace") as Int,
                                call.argument<Int>("borderType") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "blur" -> {
                    try {
                        BlurFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<ArrayList<Double>>("kernelSize") as ArrayList<Double>,
                                call.argument<ArrayList<Double>>("anchorPoint") as ArrayList<Double>,
                                call.argument<Int>("borderType") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "boxFilter" -> {
                    try {
                        BoxFilterFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("outputDepth") as Int,
                                call.argument<ArrayList<Double>>("kernelSize") as ArrayList<Double>,
                                call.argument<ArrayList<Double>>("anchorPoint") as ArrayList<Double>,
                                call.argument<Boolean>("normalize") as Boolean,
                                call.argument<Int>("borderType") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "dilate" -> {
                    try {
                        DilateFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<ArrayList<Double>>("kernelSize") as ArrayList<Double>,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "erode" -> {
                    try {
                        ErodeFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<ArrayList<Double>>("kernelSize") as ArrayList<Double>,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "filter2D" -> {
                    try {
                        Filter2DFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("outputDepth") as Int,
                                call.argument<ArrayList<Int>>("kernelSize") as ArrayList<Int>,
                                result
                        )
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "gaussianBlur" -> {
                    try {
                        GaussianBlurFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<ArrayList<Double>>("kernelSize") as ArrayList<Double>,
                                call.argument<Double>("sigmaX") as Double,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "laplacian" -> {
                    try {
                        LaplacianFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("depth") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "medianBlur" -> {
                    try {
                        MedianBlurFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("kernelSize") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "morphologyEx" -> {
                    try {
                        MorphologyExFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("operation") as Int,
                                call.argument<ArrayList<Int>>("kernelSize") as ArrayList<Int>,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "pyrMeanShiftFiltering" -> {
                    try {
                        PyrMeanShiftFilteringFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Double>("spatialWindowRadius") as Double,
                                call.argument<Double>("colorWindowRadius") as Double,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "scharr" -> {
                    try {
                        ScharrFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("depth") as Int,
                                call.argument<Int>("dx") as Int,
                                call.argument<Int>("dy") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "sobel" -> {
                    try {
                        SobelFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("depth") as Int,
                                call.argument<Int>("dx") as Int,
                                call.argument<Int>("dy") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "sqrBoxFilter" -> {
                    try {
                        SqrBoxFilterFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("outputDepth") as Int,
                                call.argument<ArrayList<Double>>("kernelSize") as ArrayList<Double>,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                //Module: Color Maps
                "applyColorMap" -> {
                    try {
                        //colorMap: Int
                        ApplyColorMapFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("colorMap") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: " + e.message, e)
                    }
                }
                //Module: Color Space Conversions
                "cvtColor" -> {
                    try {
                        CvtColorFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("outputType") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                //Module: Miscellaneous Image Transformations
                "adaptiveThreshold" -> {
                    try {
                        AdaptiveThresholdFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Double>("maxValue") as Double,
                                call.argument<Int>("adaptiveMethod") as Int,
                                call.argument<Int>("thresholdType") as Int,
                                call.argument<Int>("blockSize") as Int,
                                call.argument<Double>("constantValue") as Double,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "distanceTransform" -> {
                    //distanceType: Int, maskSize: Int
                    try {
                        DistanceTransformFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Int>("distanceType") as Int,
                                call.argument<Int>("maskSize") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }
                "threshold" -> {
                    try {
                        ThresholdFactory.process(
                                call.argument<Int>("pathType") as Int,
                                call.argument<String>("pathString") as String,
                                call.argument<ByteArray>("data") as ByteArray,
                                call.argument<Double>("thresholdValue") as Double,
                                call.argument<Double>("maxThresholdValue") as Double,
                                call.argument<Int>("thresholdType") as Int,
                                result)
                    } catch (e: Exception) {
                        result.error("OpenCV-Error", "Android: "+e.message, e)
                    }
                }

                else -> result.notImplemented()
            }
        }


    }






}
