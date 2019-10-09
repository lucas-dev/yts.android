package com.alvefard.yify.presentation.screens.details

import com.alvefard.yify.data.api.OmdbApi
import com.alvefard.yify.data.api.YifyApi
import com.alvefard.yify.data.entities.DetailsMapper
import com.alvefard.yify.data.entities.MovieMapper
import com.alvefard.yify.data.repository.DetailsRepositoryImpl
import com.alvefard.yify.domain.repositories.DetailsRepository
import com.alvefard.yify.domain.repositories.FavsRepository
import com.alvefard.yify.domain.usecases.DetailsUseCase
import com.alvefard.yify.presentation.common.ux.imagepreviewer.ImagePreviewer
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.common.ux.sharemanager.ShareManager
import com.alvefard.yify.presentation.common.ux.toastnotificator.ToastNotificator
import com.alvefard.yify.presentation.common.ux.torrentmanager.TorrentManager
import com.alvefard.yify.presentation.common.ux.videopreviewer.VideoPreviewer
import com.alvefard.yify.presentation.common.ux.webviewloader.WebViewLoader
import com.alvefard.yify.presentation.screens.landing.di.rx.schedulers.BaseSchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {
    @Provides
    fun provideDetailsPresenter(useCase: DetailsUseCase,
                                schedulerProvider: BaseSchedulerProvider,
                                tabHelper: WebViewLoader,
                                videoPreviewer: VideoPreviewer,
                                screenNavigator: ScreenNavigatorConductor,
                                imagePreviewerViewer: ImagePreviewer,
                                shareClass: ShareManager,
                                torrentOpener: TorrentManager,
                                toastHelper: ToastNotificator): DetailsContract.Presenter = DetailsPresenter(useCase, schedulerProvider, tabHelper, videoPreviewer, screenNavigator, imagePreviewerViewer, shareClass, torrentOpener, toastHelper)

    @Provides
    fun provideDetailsModel(repository: DetailsRepository): DetailsUseCase = DetailsUseCase(repository)

    @Provides
    fun provideDetailsRepository(yifyApiClient: YifyApi,
                                 omdbApiClient: OmdbApi,
                                 repository: FavsRepository): DetailsRepository = DetailsRepositoryImpl(yifyApiClient, omdbApiClient, repository, DetailsMapper(), MovieMapper())

}