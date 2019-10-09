package com.alvefard.yify.presentation.screens.search

import android.os.Build
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
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
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.queryTextChangeEvents
import io.reactivex.Observable
import io.supercharge.shimmerlayout.ShimmerLayout
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchScreen : BaseController(), SearchContract.View {
    @Inject
    lateinit var presenter: SearchContract.Presenter
    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    @BindView(R.id.iv_menu)
    lateinit var btnMenu: View
    @BindView(R.id.searchView)
    lateinit var searchView: SearchView
    @BindView(R.id.vf_movies)
    lateinit var vfMovies: ViewFlipper

    @BindView(R.id.shimmer)
    lateinit var viewShimmer: ShimmerLayout

    @BindView(R.id.rv_content)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.tv_error_msg)
    lateinit var tvErrorMsg: TextView
    @BindView(R.id.btn_retry)
    lateinit var btnErrorRetry: View

    @BindView(R.id.load_more_indicator)
    lateinit var viewLoadMore: View
    @BindView(R.id.tv_loading_indicator_message)
    lateinit var tvLoadMore: TextView
    @BindView(R.id.vf_loading_indicator)
    lateinit var vfLoadingIndicator: ViewFlipper

    private lateinit var rvAdapter: GenericAdapter<Movie>

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        screenNavigator.unlockSideMenu()
    }

    override fun onCreateView() {
        presenter.attachView(this)

        setUpRecyclerView()

        with(searchView) {
            setIconifiedByDefault(true)
            isFocusable = true
            isIconified = false
            clearFocus()
            requestFocusFromTouch()
        }

        presenter.start()
    }

    private fun setUpRecyclerView() {
        val layout = GridLayoutManager(activity, 2)
        rvAdapter = object : GenericAdapter<Movie>() {

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
        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = layout
            adapter = rvAdapter
        }
    }

    override fun updateData(movies: MutableList<Movie>) {
        rvAdapter.updateData(movies)
    }

    override fun observeMovieClick(): Observable<Movie> {
        return rvAdapter.viewClickObserable
    }

    override fun observeMenuButton(): Observable<*> {
        return btnMenu.clicks()
    }

    override fun showLoadingScreen() {
        vfMovies.displayedChild = 1
        viewShimmer.startShimmerAnimation()
    }

    override fun showErrorScreen(errorMsg: String) {
        Timber.e("Error loading search results: $errorMsg")
        viewShimmer.stopShimmerAnimation()
        tvErrorMsg.text = errorMsg
        vfMovies.displayedChild = 2
    }

    override fun showNoResultsScreen() {
        Timber.w("No results found")
        vfMovies.displayedChild = 3
    }

    override fun showContentScreen() {
        viewShimmer.stopShimmerAnimation()
        vfMovies.displayedChild = 4
    }

    override fun observeRetryLoadingButtonClick(): Observable<Unit> {
        return btnErrorRetry.clicks()
    }

    override fun inputSearchChanges(): Observable<String> {
        return searchView.queryTextChangeEvents()
                .filter { (_, _, isSubmitted) -> isSubmitted }
                .debounce(500, TimeUnit.MILLISECONDS)
                .map { (_, queryText) -> queryText.toString() }
    }

    override fun listRequestNewPage(): Observable<Boolean> {
        return recyclerView.scrollStateChanges().map {
            val isBottomReached = !recyclerView.canScrollVertically(1)
            isBottomReached && viewLoadMore.visibility == View.GONE
        }.debounce(500, TimeUnit.MILLISECONDS).filter { aBoolean: Boolean -> aBoolean }
    }

    override fun showLoadingMoreIndicator() {
        viewLoadMore.visibility = View.VISIBLE
        vfLoadingIndicator.displayedChild = 0
        tvLoadMore.text = activity!!.getText(R.string.loading)
        tvLoadMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources!!.getDimension(R.dimen.all_6sp))
    }

    override fun hideLoadingMoreIndicator() {
        viewLoadMore.visibility = View.GONE
    }

    override fun showLoadingMoreErrorIndicator(errorMsg: String) {
        Timber.e("Error loading more search results: $errorMsg")
        Handler().postDelayed({ hideLoadingMoreIndicator() }, 2000)
        vfLoadingIndicator.displayedChild = 1
        tvLoadMore.text = errorMsg
        tvLoadMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources!!.getDimension(R.dimen.all_5sp))
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun injectDependencies() {
        App.moviesPagerComponent.inject(this)
    }

}