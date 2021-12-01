package values

import android.content.Context
import com.example.spire.R
import com.example.spire.fragments.SearchFragment
import java.security.AccessControlContext

class Datasource(val context: SearchFragment) {
    fun getGameList(): Array<String> {


        return context.resources.getStringArray(R.array.games_name)

    }
}