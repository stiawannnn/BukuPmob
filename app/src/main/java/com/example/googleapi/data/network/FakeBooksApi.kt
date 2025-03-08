package com.example.googleapi.data.network

import com.example.googleapi.model.ImageLinks
import com.example.googleapi.model.VolumeInfo

object FakeBooksApi {
    fun getFakeBooks(): List<VolumeInfo> {
        return listOf(
            VolumeInfo(
                title = "The Catcher in theRye",
                authors = listOf("J.D. Salinger"),
                imageLinks = ImageLinks(
                    smallThumbnail = "",
                    thumbnail = "https://npr.brightspotcdn.com/dims4/default/f735808/2147483647/strip/true/crop/363x574+0+0/resize/880x1392!/quality/90/?url=http%3A%2F%2Fnpr-brightspot.s3.amazonaws.com%2Flegacy%2Fsites%2Fwkar%2Ffiles%2Fcatcher_in_the_rye_cover.png"
                )
            ),
            VolumeInfo(
                title = "1984",
                authors = listOf("George Orwell"),
                imageLinks = ImageLinks(
                    smallThumbnail = "https://upload.wikimedia.org/wikipedia/en/c/c3/1984first.jpg",
                    thumbnail = "https://i0.wp.com/literariness.org/wp-content/uploads/2019/05/197d462dc80434cf046557946d636309.jpg?resize=700%2C1043&ssl=1"
                )
            ),
            VolumeInfo(
                title = "To Kill a Mockingbird",
                authors = listOf("Harper Lee"),
                imageLinks = ImageLinks(
                    smallThumbnail = "https://upload.wikimedia.org/wikipedia/en/7/79/To_Kill_a_Mockingbird.JPG",
                    thumbnail = "https://play-lh.googleusercontent.com/YFjwVyUQzQN75eK4zO7QUEd9njzP5Gyt2PF0WKpvjZmQV3RRxhzzaiVC-wfV5gAboU8N=w240-h480-rw"
                )
            )
        )
    }
}
