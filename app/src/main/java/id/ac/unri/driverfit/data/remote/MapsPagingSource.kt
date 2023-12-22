import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.ac.unri.driverfit.data.remote.payload.Place
import id.ac.unri.driverfit.domain.usecase.GetRestPlaceUseCase

class MapsPagingSource(
    private val getRestPlaceUseCase: GetRestPlaceUseCase,
    private val lat: Double,
    private val lng: Double,
) : PagingSource<String, Place>() {
    override fun getRefreshKey(state: PagingState<String, Place>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Place> {
        return try {
            val currentPage = params.key
            val response = getRestPlaceUseCase(
                lat = lat,
                lng = lng,
                pageToken = currentPage
            )
            val nextPage = response.nextPageToken

            LoadResult.Page(
                data = response.place,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}