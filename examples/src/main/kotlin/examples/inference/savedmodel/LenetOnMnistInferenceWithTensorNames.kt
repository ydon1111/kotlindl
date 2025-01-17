/*
 * Copyright 2020-2023 JetBrains s.r.o. and Kotlin Deep Learning project contributors. All Rights Reserved.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

package examples.inference.savedmodel

import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.inference.savedmodel.SavedModel
import org.jetbrains.kotlinx.dl.dataset.embedded.mnist
import org.jetbrains.kotlinx.dl.dataset.evaluate
import org.jetbrains.kotlinx.dl.dataset.predict

private const val PATH_TO_MODEL = "examples/src/main/resources/models/savedmodel"

/**
 * This examples demonstrates running [SavedModel] for prediction on [mnist] dataset.
 *
 * It uses string tensor names to get access to input/output tensors in TensorFlow static graph.
 */
fun lenetOnMnistInferenceWithTensorNames() {
    val (train, test) = mnist()

    SavedModel.load(PATH_TO_MODEL).use {
        println(it.graphToString())

        val prediction = it.predict(train.getX(0), "Placeholder", "ArgMax")

        println("Predicted Label is: $prediction")
        println("Correct Label is: " + train.getY(0))

        val predictions = it.predict(test) { data -> predict(data, "Placeholder", "ArgMax") }
        println(predictions.toString())

        println("Accuracy is : ${it.evaluate(test, Metrics.ACCURACY, SavedModel::predict)}")
    }
}

/** */
fun main(): Unit = lenetOnMnistInferenceWithTensorNames()
