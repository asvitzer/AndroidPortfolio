/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alvinsvitzer.flixbook;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alvinsvitzer.flixbook.data.AppRepository;
import com.alvinsvitzer.flixbook.data.local.MovieLocalDataStore;
import com.alvinsvitzer.flixbook.data.remote.MovieRemoteDataStore;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Will try to use this setup to inject mock implementations of the data store to be able to
 * more effectively using Espresso UI testing since I can override the mock version to use static data.
 */
public class Injection {

    public static AppRepository provideMovieDataStoreRepository(@NonNull Context context) {

        checkNotNull(context);

        return AppRepository.getInstance(MovieRemoteDataStore.getInstance(context)
                , MovieLocalDataStore.getInstance());
    }

    public static MovieRemoteDataStore provideRemoteDataSource(@NonNull Context context) {

        checkNotNull(context);

        return MovieRemoteDataStore.getInstance(context);
    }

    public static MovieLocalDataStore provideLocalDataSource() {

        return MovieLocalDataStore.getInstance();
    }

}
