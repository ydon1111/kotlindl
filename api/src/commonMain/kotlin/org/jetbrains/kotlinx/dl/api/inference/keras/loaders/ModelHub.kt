/*
 * Copyright 2020-2022 JetBrains s.r.o. and Kotlin Deep Learning project contributors. All Rights Reserved.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

package org.jetbrains.kotlinx.dl.api.inference.keras.loaders

import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import java.io.File
import java.nio.file.Files

internal const val AWS_S3_URL: String = "https://kotlindl.s3.amazonaws.com"

/**
 * This is an abstract class which provides methods for loading models, its weights and labels (for prediction purposes) to the local [cacheDirectory].
 *
 * @property [cacheDirectory] The directory for all loaded models and datasets. It should be created before model loading and should have all required permissions for file writing/reading on your OS.
 *
 * @since 0.2
 */
public abstract class ModelHub {
    /** Url of the model storage. */
    protected val awsS3Url: String = AWS_S3_URL

    /**
     * Loads model configuration without weights.
     *
     * @param [modelType] This unique identifier defines the way to the S3 bucket with the model and its weights and the local directory for the model and its weights.
     * @param [loadingMode] Strategy of existing model use-case handling.
     * @return Raw model without weights. Needs in compilation and weights loading before usage.
     */
    public abstract fun <T : InferenceModel, U : InferenceModel> loadModel(
        modelType: ModelType<T, U>,
        loadingMode: LoadingMode = LoadingMode.SKIP_LOADING_IF_EXISTS
    ): T

    /**
     * Loads pretrained model of [modelType] from the ModelHub in [loadingMode].
     *
     * @param [modelType] This unique identifier defines the way to the S3 bucket with the model and its weights and the local directory for the model and its weights.
     * @param [loadingMode] Strategy of existing model use-case handling.
     * @return Pretrained model.
     */
    public fun <T : InferenceModel, U : InferenceModel> loadPretrainedModel(
        modelType: ModelType<T, U>,
        loadingMode: LoadingMode = LoadingMode.SKIP_LOADING_IF_EXISTS
    ): U {
        return modelType.pretrainedModel(this)
    }

    /**
     *
     */
    @Suppress("UNCHECKED_CAST")
    public operator fun <T : InferenceModel, U : InferenceModel> get(modelType: ModelType<T, U>): U {
        return loadPretrainedModel(modelType = modelType)
    }
}


