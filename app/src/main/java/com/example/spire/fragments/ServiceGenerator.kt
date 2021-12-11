import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private const val BASE_URL = "https://rawg.io"
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = builder.build()
    private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val httpClient = OkHttpClient.Builder()
    fun <S> createService(serviceClass: Class<S>?): S {
        if (!httpClient.interceptors().contains(loggingInterceptor)) {
            httpClient.addInterceptor(loggingInterceptor)
            builder.client(httpClient.build())
        }
        return retrofit.create(serviceClass)
    }
}