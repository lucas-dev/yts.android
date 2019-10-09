package com.alvefard.yify.presentation.screens.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.OnClick
import com.alvefard.yify.domain.model.*
import com.alvefard.yify.presentation.App
import com.alvefard.yify.presentation.common.GenericAdapter
import com.alvefard.yify.presentation.common.base.BaseContract
import com.alvefard.yify.presentation.common.base.BaseController
import com.alvefard.yify.presentation.common.ux.imageloader.ImageLoader
import com.alvefard.yify.presentation.common.ux.screennavigator.ScreenNavigatorConductor
import com.alvefard.yify.presentation.screens.details.viewholders.CastViewHolder
import com.alvefard.yify.presentation.screens.details.viewholders.DownloadsViewHolder
import com.alvefard.yify.presentation.screens.details.viewholders.GalleryViewHolder
import com.alvefard.yify.presentation.screens.details.viewholders.SuggestionViewHolder
import com.alvefard.yify.presentation.util.BundleBuilder
import com.alvefard.yify.presentation.util.MoviesDiffUtil
import com.alvefard.yify.presentation.util.StringUtils
import com.alvefard.yifymovies.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import javax.inject.Inject

class DetailsScreen(bundle: Bundle) : BaseController(bundle), DetailsContract.View {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var screenNavigator: ScreenNavigatorConductor

    @Inject
    lateinit var presenter: DetailsContract.Presenter

    @BindView(R.id.iv_bkg)
    lateinit var ivBackground: ImageView
    @BindView(R.id.iv_cover)
    lateinit var ivCover: ImageView
    @BindView(R.id.tv_title)
    lateinit var tvTitle: TextView
    @BindView(R.id.tv_runtime)
    lateinit var tvRuntime: TextView
    @BindView(R.id.tv_releasedate)
    lateinit var tvReleaseDate: TextView
    @BindView(R.id.tv_rating_imdb)
    lateinit var tvRatingImdb: TextView
    @BindView(R.id.tv_rating_rt)
    lateinit var tvRatingRt: TextView
    @BindView(R.id.tv_genres)
    lateinit var tvGenres: TextView
    @BindView(R.id.tv_download_count)
    lateinit var tvDownloadCount: TextView
    @BindView(R.id.tv_plot_description)
    lateinit var tvDescription: TextView
    @BindView(R.id.tv_awards)
    lateinit var tvAwards: TextView
    @BindView(R.id.tv_pg)
    lateinit var tvPg: TextView
    @BindView(R.id.tv_country)
    lateinit var tvCountry: TextView
    @BindView(R.id.tv_language)
    lateinit var tvLanguage: TextView
    @BindView(R.id.cv_trailer)
    lateinit var cvTrailer: CardView
    @BindView(R.id.cv_yify)
    lateinit var cvYify: CardView
    @BindView(R.id.cv_imdb)
    lateinit var cvImdb: CardView
    @BindView(R.id.rv_cast)
    lateinit var rvCast: RecyclerView
    @BindView(R.id.rv_suggestions)
    lateinit var rvSuggestions: RecyclerView
    @BindView(R.id.rv_download)
    lateinit var rvDownloads: RecyclerView
    @BindView(R.id.vf_download_count)
    lateinit var vfDownloadCount: ViewFlipper
    @BindView(R.id.vf_country)
    lateinit var vfCountry: ViewFlipper
    @BindView(R.id.vf_awards)
    lateinit var vfAwards: ViewFlipper
    @BindView(R.id.vf_rating_imdb)
    lateinit var vfRatingImdb: ViewFlipper
    @BindView(R.id.vf_rating_rt)
    lateinit var vfRatingRt: ViewFlipper
    @BindView(R.id.vf_pg)
    lateinit var vfPg: ViewFlipper
    @BindView(R.id.vf_suggestions)
    lateinit var vfSuggestions: ViewFlipper
    @BindView(R.id.vf_cast)
    lateinit var vfCast: ViewFlipper
    @BindView(R.id.btn_fav)
    lateinit var btnFav: FloatingActionButton
    @BindView(R.id.btn_back)
    lateinit var btnBack: FloatingActionButton
    @BindView(R.id.btn_gallery)
    lateinit var btnGallery: FloatingActionButton
    @BindView(R.id.btn_share)
    lateinit var btnShare: FloatingActionButton
    @BindView(R.id.btn_home)
    lateinit var btnHome: FloatingActionButton
    @BindView(R.id.bs_gallery)
    lateinit var bsGallery: View
    @BindView(R.id.rv_gallery)
    lateinit var rvGallery: RecyclerView

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var movie: Movie

    private lateinit var suggestionsAdapter: GenericAdapter<Movie>
    private lateinit var castAdapter: GenericAdapter<Cast>
    private lateinit var downloadsAdapter: GenericAdapter<Torrent>
    private lateinit var galleryAdapter: GenericAdapter<String>

    constructor(details: Movie) : this(BundleBuilder(Bundle()).putSerializable(MOVIE_DETAILS, details).build())

    init {
        retainViewMode = RetainViewMode.RETAIN_DETACH
    }

    override fun onAttach(view: View) {
        presenter.checkIsFav()
        screenNavigator.lockSideMenu()
    }

    override fun onCreateView() {
        presenter.attachView(this)
        setUpCastRecyclerView()
        setUpSuggestionsRecyclerView()
        setUpGalleryRecyclerView()
        setUpButtonLinks()

        movie = getMovie()

        setUpBasicViews()

        setUpBottomSheet()

        presenter.start()
    }

    override fun getPresenter(): BaseContract.Presenter<*> {
        return presenter
    }

    override fun getLayoutId(): Int {
        return R.layout.details_screen
    }

    override fun injectDependencies() {
        App.moviesPagerComponent.inject(this)
    }

    private fun setUpButtonLinks() {
        activity?.let { activity ->
            (cvTrailer.findViewById<View>(R.id.iv_link) as ImageView).setImageDrawable(activity.getDrawable(R.drawable.ic_youtube))
            (cvTrailer.findViewById<View>(R.id.tv_link) as TextView).text = activity.resources.getString(R.string.trailer)
            (cvYify.findViewById<View>(R.id.iv_link) as ImageView).setImageDrawable(activity.getDrawable(R.drawable.ic_yts))
            (cvYify.findViewById<View>(R.id.tv_link) as TextView).text = activity.resources.getString(R.string.yify)
            (cvImdb.findViewById<View>(R.id.iv_link) as ImageView).setImageDrawable(activity.getDrawable(R.drawable.imdb))
            (cvImdb.findViewById<View>(R.id.tv_link) as TextView).text = activity.resources.getString(R.string.imdb)
        }
    }

    private fun setUpBasicViews() {
        tvDescription.text = movie.description
        tvTitle.text = movie.title
        tvRuntime.text = StringUtils.getFormattedRuntime(movie.runtime)
        tvGenres.text = movie.genres.toString()
        tvLanguage.text = movie.language
        imageLoader.loadImage(movie.coverMedium, ivCover)
        imageLoader.loadImage(movie.coverMedium, ivBackground)
        tvReleaseDate.text = movie.releaseDate.toString()

        setUpDownloadsRecyclerView()
        downloadsAdapter.updateData(movie.torrents)
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bsGallery)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun showYifyLoading() {
        vfDownloadCount.displayedChild = 0
    }

    override fun showYifyContent(detail: Detail) {
        vfDownloadCount.displayedChild = 1
        tvDownloadCount.text = detail.downloadCount.toString()
    }

    override fun fillGalleryItems(items: MutableList<String>) {
        galleryAdapter.updateData(items)
    }

    override fun showOmdbLoading() {
        vfCountry.displayedChild = 0
        vfAwards.displayedChild = 0
        vfRatingImdb.displayedChild = 0
        vfRatingRt.displayedChild = 0
        vfPg.displayedChild = 0
    }

    override fun showOmdbContent(omdb: Omdb) {
        vfCountry.displayedChild = 1
        vfAwards.displayedChild = 1
        vfRatingImdb.displayedChild = 1
        vfRatingRt.displayedChild = 1
        vfPg.displayedChild = 1

        tvCountry.text = omdb.country
        tvAwards.text = omdb.awards
        tvRatingImdb.text = omdb.imdbRating
        tvRatingRt.text = omdb.rtRating
        tvPg.text = StringUtils.getRatedInfo(omdb.rated)
    }

    override fun observeYifyLink(): Observable<*> {
        return cvYify.clicks()
    }

    override fun observeImdbLink(): Observable<*> {
        return cvImdb.clicks()
    }

    override fun observeBtnTrailer(): Observable<*> {
        return cvTrailer.clicks()
    }

    override fun observeBackButton(): Observable<*> {
        return btnBack.clicks()
    }

    override fun observeGalleryButton(): Observable<*> {
        return btnGallery.clicks()
    }

    override fun observeShareButton(): Observable<*> {
        return btnShare.clicks()
    }

    override fun observeHomeButton(): Observable<*> {
        return btnHome.clicks()
    }

    override fun observeImageClick(): Observable<*> {
        return ivCover.clicks()
    }

    override fun observeCastItemClick(): Observable<Cast> {
        return castAdapter.viewClickObserable
    }

    override fun observeTorrentItemClick(): Observable<Torrent> {
        return downloadsAdapter.viewClickObserable
    }

    override fun observeGalleryItemClick(): Observable<String> {
        return galleryAdapter.viewClickObserable
    }

    override fun showSuggestionsLoading() {
        vfSuggestions.displayedChild = 0
    }

    override fun showSuggestions(movies: MutableList<Movie>) {
        vfSuggestions.displayedChild = 1
        suggestionsAdapter.updateData(movies)
    }

    override fun showCastLoading() {
        vfCast.displayedChild = 0
    }

    override fun showCastContent(castList: MutableList<Cast>) {
        vfCast.displayedChild = 1
        castAdapter.updateData(castList)
    }

    override fun observeMovieSuggestionClick(): Observable<Movie> {
        return suggestionsAdapter.viewClickObserable
    }

    override fun observeBtnFav(): Observable<*> {
        return btnFav.clicks()
    }

    override fun setFavIconOn() {
        btnFav.setImageDrawable(resources!!.getDrawable(R.drawable.fav_on))
    }

    override fun setFavIconOff() {
        btnFav.setImageDrawable(resources!!.getDrawable(R.drawable.fav_off))
    }

    override fun getMovie(): Movie {
        return args.getSerializable(MOVIE_DETAILS) as Movie
    }

    override fun expandGallery() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        else
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setUpSuggestionsRecyclerView() {
        rvSuggestions.setHasFixedSize(true)
        rvSuggestions.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        suggestionsAdapter = object : GenericAdapter<Movie>() {

            override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                    SuggestionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.suggestion_item, parent, false))

            override fun onBindData(holder: RecyclerView.ViewHolder, `val`: Movie) {
                with(holder as SuggestionViewHolder) {
                    tvTitle.text = `val`.title
                    tvYear.text = `val`.releaseDate.toString()
                    rbSuggestions.rating = `val`.rating.toFloat() / 2
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
        rvSuggestions.adapter = suggestionsAdapter
    }

    private fun setUpGalleryRecyclerView() {
        rvGallery.setHasFixedSize(true)
        val layout = GridLayoutManager(activity, 3)
        rvGallery.layoutManager = layout

        galleryAdapter = object : GenericAdapter<String>() {

            override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
                return GalleryViewHolder(view)
            }

            override fun onBindData(holder: RecyclerView.ViewHolder, `val`: String) {
                val viewHolder = holder as GalleryViewHolder
                if (`val`.isNotEmpty()) imageLoader.loadImage(`val`, viewHolder.imageView)
            }

            override fun updateData(items: MutableList<String>) {
                getItems().addAll(items)
                notifyDataSetChanged()
            }
        }
        rvGallery.adapter = galleryAdapter
    }

    private fun setUpCastRecyclerView() {
        castAdapter = object : GenericAdapter<Cast>() {

            override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
                return CastViewHolder(view)
            }

            override fun onBindData(holder: RecyclerView.ViewHolder, cast: Cast) {
                with(holder as CastViewHolder) {
                    tvActor.text = cast.name
                    tvCharacter.text = cast.character
                    imageLoader.loadImage(cast.image, imageView)
                }
            }

            override fun updateData(items: MutableList<Cast>) {
                getItems().addAll(items)
                notifyDataSetChanged()
            }
        }

        rvCast.adapter = castAdapter
        rvCast.setHasFixedSize(true)
        rvCast.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpDownloadsRecyclerView() {
        downloadsAdapter = object : GenericAdapter<Torrent>() {

            override fun setViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.download_item, parent, false)
                return DownloadsViewHolder(view)
            }

            override fun onBindData(holder: RecyclerView.ViewHolder, torrent: Torrent) {
                with(holder as DownloadsViewHolder) {
                    tvTitle.text = torrent.quality
                    tvFormat.text = torrent.type
                    tvSize.text = torrent.size
                    activity?.let {activity ->
                        tvPeers.text = activity.resources.getString(R.string.peers, torrent.peers.toString())
                        tvSeeds.text = activity.resources.getString(R.string.seeds, torrent.seeds.toString())
                    }
                }
            }

            override fun updateData(items: MutableList<Torrent>) {
                getItems().addAll(items)
                notifyDataSetChanged()
            }
        }

        rvDownloads.adapter = downloadsAdapter
        rvDownloads.setHasFixedSize(true)
        rvDownloads.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    //   sorry ( ɵ̥̥ ˑ̫ ɵ̥̥ )
    @OnClick(R.id.fake_back_button)
    fun fakeBackButtonClick() {
        btnBack.performClick()
    }


    @OnClick(R.id.fake_gallery_button)
    fun fakeGalleryButtonClick() {
        btnGallery.performClick()
    }

    @OnClick(R.id.fake_share_button)
    fun fakeShareButtonClick() {
        btnShare.performClick()
    }

    @OnClick(R.id.fake_home_button)
    fun fakeHomeButtonClick() {
        btnHome.performClick()
    }

    @OnClick(R.id.fake_container)
    fun fakeContainerClick() {
        ivBackground.performClick()
    }

    companion object {
        private const val MOVIE_DETAILS = "movie_details"
    }
}