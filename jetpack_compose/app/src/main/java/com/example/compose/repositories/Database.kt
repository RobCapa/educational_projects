package com.example.compose.repositories

import com.example.compose.data.*

object Database {

    val series = listOf(
        Series(
            1,
            "Rick and Morty",
            "https://blogscdn.thehut.net/wp-content/uploads/sites/499/2018/09/24160403/rick-and-morty.jpeg",
            "https://wallpapers.com/images/featured/8rc57d4ds44gqzau.jpg",
        ),
        Series(
            2,
            "Family Guy",
            "https://play-images-prod-ctf.tech.tvnz.co.nz/api/v1/web/image/3KpJt9TOOerhWO1OwJs2e/c708b2057e34c17039ff6755730ddf49/FamilyGuy_coverimg_v2.png.c708b2057e34c17039ff6755730ddf49.jpg?width=1920&height=548",
            "https://wallpaperaccess.com/full/335129.jpg",
        ),
    )

    val seasons = listOf(
        Season(
            1,
            1,
            "https://metro.co.uk/wp-content/uploads/2020/05/rick-morty-41-01b4.jpeg?quality=90&strip=all&crop=0px%2C64px%2C915px%2C481px&resize=1200%2C630"
        ),
        Season(
            1,
            2,
            "https://static1.colliderimages.com/wordpress/wp-content/uploads/2021/09/rick-morty-dynamic.jpg"
        ),
        Season(
            1,
            3,
            "https://avatars.mds.yandex.net/get-kinopoisk-post-img/1642096/06facdb4aadc45efd17d058788ab3c01/960x540"
        ),

        Season(
            2,
            1,
            "https://s.foxdcg.com/dcg/img/Fox_Networks_DCG_-_FOX_Broadcasting/420/948/FamilyGuy_BendorBlockbuster.jpg?fit=inside%7C480:270"
        ),
        Season(
            2,
            2,
            "https://d32qys9a6wm9no.cloudfront.net/images/tvs/episodes/cf/798266236c7_706x397.jpg?t=1638460201"
        ),
        Season(
            2,
            3,
            "https://caracoltv.brightspotcdn.com/dims4/default/5b6cb8d/2147483647/strip/true/crop/1300x946+0+0/resize/1000x728!/quality/90/?url=http%3A%2F%2Fcaracol-brightspot.s3.amazonaws.com%2F29%2Fbe%2Fe9fe1c4848808e17a0a90bf6851f%2Ffamily-guy-star.jpg"
        ),
    )

    val episodes = listOf(
        Episode(
            1,
            1,
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://static1.pocketlintimages.com/wordpress/wp-content/uploads/162179-tv-news-feature-rick-and-morty-season-6-release-date-trailer-and-how-to-watch-image1-kbmgzwpsy5.jpg"
        ),
        Episode(
            1,
            1,
            2,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://www.premiere.fr/sites/default/files/styles/partage_rs/public/2021-03/rick-and-morty-s5-1617114015967.jpg",
        ),
        Episode(
            1,
            1,
            3,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://www.denofgeek.com/wp-content/uploads/2022/09/rick-and-morty-season-6-episode-4.jpg"
        ),

        Episode(
            1,
            2,
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://variety.com/wp-content/uploads/2022/07/rick-and-morty-season-6.jpg?w=1000"
        ),
        Episode(
            1,
            2,
            2,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://m.media-amazon.com/images/M/MV5BZmZhNWMyODgtMzA0OC00NWFhLTllODQtYmJkZjYxYWU4MGU1XkEyXkFqcGdeQWFybm8@._V1_.jpg"
        ),
        Episode(
            1,
            2,
            3,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://www.nme.com/wp-content/uploads/2022/10/Rick_And_Morty_Portal_Gun_JuRicksic_Mort.jpg"
        ),

        Episode(
            1,
            3,
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://variety.com/wp-content/uploads/2021/06/Rick-and-Morty-Merch.jpg?w=681&h=383&crop=1"
        ),
        Episode(
            1,
            3,
            2,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://images.squarespace-cdn.com/content/v1/54fc8146e4b02a22841f4df7/1572526237662-KHRA3V0X1YKLVCU9E9K0/image-asset.jpeg"
        ),
        Episode(
            1,
            3,
            3,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://www.radiofrance.fr/s3/cruiser-production/2019/10/22f8d83b-2dbb-4156-8f6d-9cc13b94e16f/1200x680_rickmorty.jpg"
        ),

        /* */

        Episode(
            2,
            1,
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://m.media-amazon.com/images/M/MV5BNTM2YWE0YmQtM2JmMC00ZDgxLTllNDktZTFlYWU5OGVkNWU0XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_FMjpg_UX1000_.jpg"
        ),
        Episode(
            2,
            1,
            2,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://pm1.narvii.com/8173/fb02f18479810d38b389a7e4b4b4e65609fbe306r1-1280-720v2_hq.jpg",
        ),
        Episode(
            2,
            1,
            3,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://static.wikia.nocookie.net/familyguy/images/b/bd/Edibles.png/revision/latest?cb=20190401025246"
        ),

        Episode(
            2,
            2,
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://www.hollywoodreporter.com/wp-content/uploads/2013/11/family_guy_brian.jpg"
        ),
        Episode(
            2,
            2,
            2,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://www.looper.com/img/gallery/the-worst-things-brian-griffin-has-ever-done-on-family-guy/l-intro-1669128726.jpg"
        ),
        Episode(
            2,
            2,
            3,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://i.ytimg.com/vi/TDS50jbK2BA/maxresdefault.jpg"
        ),

        Episode(
            2,
            3,
            1,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://readysteadycut.com/wp-content/uploads/2020/11/kacx03_031_03-0016-1600x900-1.jpg"
        ),
        Episode(
            2,
            3,
            2,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://i.ytimg.com/vi/DRPHXzBcACw/maxresdefault.jpg"
        ),
        Episode(
            2,
            3,
            3,
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries",
            "https://pm1.narvii.com/8118/6ea904ff6b13ab0de23fc96e74dd2ac2551bffd1r1-1710-900v2_uhq.jpg"
        ),
    )

    val seasonMoments = listOf(
        SeasonMoment(
            1,
            1,
            "https://data-vykhoda.ru/wp-content/uploads/2017/03/37.jpg",
        ),
        SeasonMoment(
            1,
            1,
            "https://content.internetvideoarchive.com/content/hdphotos/14105/014105/014105_1344x1008_637958335203055581.jpg",
        ),
        SeasonMoment(
            1,
            1,
            "https://img.thedailybeast.com/image/upload/c_crop,d_placeholder_euli9k,h_1686,w_3000,x_0,y_0/dpr_2.0/c_limit,w_600/f_jpg/fl_lossy,q_auto/v1588568507/200503-leon-rick-morty-ease_f8zkih",
        ),
        SeasonMoment(
            1,
            2,
            "https://avatars.mds.yandex.net/i?id=e0c87bd7b0d0aee15e1166c0d95b6710-5277912-images-thumbs&n=13",
        ),
        SeasonMoment(
            1,
            2,
            "https://image-cdn.hypb.st/https%3A%2F%2Fhypebeast.com%2Fimage%2F2022%2F11%2Frick-and-morty-season-6-return-trailer-info-000.jpg?w=960&cbr=1&q=90&fit=max",
        ),
        SeasonMoment(
            1,
            2,
            "https://imgix.bustle.com/inverse/ab/9c/b0/15/fa0e/4f04/9d11/63068b00b7a5/rick-and-morty-heist-glasses.jpeg?w=1200&h=630&fit=crop&crop=faces&fm=jpg",
        ),
        SeasonMoment(
            1,
            3,
            "https://images.vertigo.com.ua/2017/08/og_1-15.jpg",
        ),
        SeasonMoment(
            1,
            3,
            "https://media.cdn.adultswim.com/uploads/20220909/thumbnails/2_2299111076-RickAndMorty_602_RickAMortWellLived.png",
        ),
        SeasonMoment(
            1,
            3,
            "https://static1.srcdn.com/wordpress/wp-content/uploads/2023/03/rick-prime-and-evil-morty.jpg",
        ),

        SeasonMoment(
            2,
            1,
            "https://tv-fanatic-res.cloudinary.com/iu/s--a19_HvKf--/t_full/cs_srgb,f_auto,fl_strip_profile.lossy,q_auto:420/v1371227848/a-very-wrong-turn.png",
        ),
        SeasonMoment(
            2,
            1,
            "https://readysteadycut.com/wp-content/uploads/2021/01/maxresdefault-2.jpg",
        ),
        SeasonMoment(
            2,
            1,
            "https://tv-fanatic-res.cloudinary.com/iu/s--yi8ZTZAM--/t_full/cs_srgb,f_auto,fl_strip_profile.lossy,q_auto:420/v1371224931/the-guys-in-jail.png",
        ),
        SeasonMoment(
            2,
            2,
            "https://content.internetvideoarchive.com/content/hdphotos/12652/012652/012652_1280x720_514492_011.jpg",
        ),
        SeasonMoment(
            2,
            2,
            "https://img1.hotstarext.com/image/upload/f_auto,t_web_hs_2_5x/sources/r1/cms/prod/3073/673073-h",
        ),
        SeasonMoment(
            2,
            2,
            "https://cdn.seat42f.com/wp-content/uploads/2023/02/26121131/FAMILY-GUY-Season-21-Episode-13-Photo-Single-White-Dad.jpg",
        ),
        SeasonMoment(
            2,
            3,
            "https://img.betaseries.com/fkDS5vQEN2B7XariigQAeD1ThgY=/500x282/smart/https%3A%2F%2Fpictures.betaseries.com%2Fbanners%2Fepisodes%2F16%2F4f82e16b112beb3437705f3f4b710f4b.jpg",
        ),
        SeasonMoment(
            2,
            3,
            "https://img1.hotstarext.com/image/upload/f_auto,t_web_hs_2_5x/sources/r1/cms/prod/3070/673070-h",
        ),
        SeasonMoment(
            2,
            3,
            "https://m.media-amazon.com/images/M/MV5BZjU3YzcwODYtNTk1NC00OGUyLWIyOTEtMWZmM2NjY2EzNGY2XkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg",
        ),
    )

    val personages = listOf(
        Personage(
            1,
            "Rick",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/rick-and-morty-image-1662104014.jpg?crop=0.315xw:0.560xh;0.351xw,0.168xh&resize=480:*"
        ),
        Personage(
            1,
            "Morty",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://static.tvtropes.org/pmwiki/pub/images/morty_smith_2.png"
        ),
        Personage(
            1,
            "Jerry",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://static.wikia.nocookie.net/rickandmorty/images/5/5c/JerrySmithPilot.webp/revision/latest?cb=20221207053146"
        ),
        Personage(
            1,
            "Summer",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://static.wikia.nocookie.net/rickandmorty/images/a/ad/Mechanical_Summer.png/revision/latest?cb=20170731155316"
        ),

        Personage(
            2,
            "Peter",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://static.wikia.nocookie.net/familyguy/images/a/aa/FamilyGuy_Single_PeterDrink_R7.jpg/revision/latest?cb=20200526171842"
        ),
        Personage(
            2,
            "Stewie",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://static.wikia.nocookie.net/familyguy/images/9/90/FamilyGuy_Single_StewieBackpack_R7.jpg/revision/latest?cb=20200526171841"
        ),
        Personage(
            2,
            "Lois",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
            "https://static.wikia.nocookie.net/villains-fr/images/8/8b/Fancy_New_Lois.jpg/revision/latest?cb=20210301184926&path-prefix=fr"
        ),
    )
}