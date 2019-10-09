package com.alvefard.yify.presentation.screens.favs

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.alvefard.yify.domain.model.Movie
import com.alvefard.yify.presentation.App
import com.alvefard.yify.presentation.common.base.BaseContract
import com.alvefard.yify.presentation.common.base.BaseController
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.common.GenericAdapter
import com.alvefard.yify.presentation.common.ux.imageloader.ImageLoader
import com.alvefard.yify.presentation.screens.common.MovieViewHolder
import com.alvefard.yify.presentation.util.MoviesDiffUtil
import com.alvefard.yify.presentation.util.StringUtils
import com.alvefard.yifymovies.R
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class FavsScreen : BaseController(), FavsContract.View {
    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    @Inject
    lateinit var presenter: FavsContract.Presenter

    @BindView(R.id.menu)
    lateinit var btnMenu: ImageView
    @BindView(R.id.view_flipper)
    lateinit var viewFlipper: ViewFlipper
    @BindView(R.id.rv_favs)
    lateinit var rvFavs: RecyclerView

    private lateinit var adapter: GenericAdapter<Movie>

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.attachView(this)
        setUpRecyclerView()
        screenNavigator.unlockSideMenu()
        presenter.start()
    }

    override fun onCreateView() {}

    private fun setUpRecyclerView() {
        rvFavs.setHasFixedSize(true)
        val layout = GridLayoutManager(activity, 2)
        rvFavs.layoutManager = layout

        adapter = object : GenericAdapter<Movie>() {

            override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
                return MovieViewHolder(view)
            }

            override fun onBindData(holder: RecyclerView.ViewHolder, `val`: Movie) {
                with(holder as MovieViewHolder) {
                    tvTitle.text = `val`.title
                    tvRating.text = `val`.rating.toString()
                    tvRuntime.text = StringUtils.getFormattedRuntime(`val`.runtime)
                    tvYear.text = `val`.releaseDate.toString()
                    imageLoader.loadImage(`val`.coverMedium, imageView)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageView.transitionName = `val`.id
                    }
                }
            }

            override fun updateData(items: MutableList<Movie>) {
                val diffUtil = MoviesDiffUtil(getItems(), items)
                val diffResult = DiffUtil.calculateDiff(diffUtil)
                getItems().clear()
                getItems().addAll(items)
                diffResult.dispatchUpdatesTo(this)
            }
        }
        rvFavs.adapter = adapter
    }

    override fun showLoadingScreen() {
        viewFlipper.displayedChild = 0
    }

    override fun showContentView() {
        viewFlipper.displayedChild = 2
    }

    override fun showEmptyView() {
        Timber.w("No movies saved")
        viewFlipper.displayedChild = 1
    }

    override fun updateData(movies: MutableList<Movie>) {
        adapter.updateData(movies)
    }

    override fun observeMovieClick(): Observable<Movie> {
        return adapter.viewClickObserable
    }

    override fun observeBtnMenu(): Observable<*> {
        return btnMenu.clicks()
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_favs
    }

    override fun injectDependencies() {
        App.moviesPagerComponent.inject(this)
    }

}