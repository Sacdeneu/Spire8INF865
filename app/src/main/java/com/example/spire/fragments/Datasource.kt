package values

import android.content.Context
import com.example.spire.R
import com.example.spire.fragments.SearchFragment
import com.example.spire.fragments.HomeFragment
import com.example.spire.fragments.FriendsFragment
import com.example.spire.fragments.HighlightedFragment
import com.example.spire.fragments.UserProfilFragment
import java.security.AccessControlContext

class Datasource(val context: SearchFragment) {
    fun getGameList(): Array<String> {

        return context.resources.getStringArray(R.array.games_name)
    }
}

class Datasource2(val context: HomeFragment) {
    fun getGameList(): Array<String> {

        return context.resources.getStringArray(R.array.games_name)
    }
}

class Datasource4(val context: HighlightedFragment) {
    fun getGameList(): Array<String> {

        return context.resources.getStringArray(R.array.games_name)
    }
}

class Datasource5(val context: UserProfilFragment) {
    fun getGameList(): Array<String> {

        return context.resources.getStringArray(R.array.games_name)
    }
}


class Datasource3(val context: FriendsFragment) {
    fun getFriendList(): Array<String> {

        return context.resources.getStringArray(R.array.friends_name)
    }
}