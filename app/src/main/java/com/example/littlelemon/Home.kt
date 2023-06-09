@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalGlideComposeApi::class)

package com.example.littlelemon

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.littlelemon.ui.theme.LittleLemonColor
import com.example.littlelemon.ui.theme.karla
import com.example.littlelemon.ui.theme.markazi
import java.util.*


@Composable
fun Home(
    navcon: NavHostController,
    databaseMenu: List<MenuItem>
) {
    var input by remember {
        mutableStateOf<String>("")
    }

    var menuItems by remember {
        mutableStateOf<List<MenuItem>>(emptyList())
    }



    Column(modifier = Modifier.fillMaxSize()) {
//        HomeHeader(navcon)
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFF495E57))
                .height(340.dp)
        ) {
            HeroSection()
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(start = 14.dp, end = 14.dp, top = 0.dp)
                    .height(50.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(fontSize = 17.sp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFF495E57),
                    backgroundColor = Color(0xFFFFFFFF),
                    focusedBorderColor = Color.Black
                ),
                placeholder = { Text(text = "Enter search phrase") }
            )
        }
        Text(text = "Order for delivery", fontWeight = FontWeight.Bold, color= LittleLemonColor.charcoal , fontFamily = karla, fontSize = 20.sp, modifier = Modifier.padding(top=10.dp, start = 10.dp, bottom = 10.dp))

        var categSet by remember{ mutableStateOf<Set<String>>(emptySet()) }

        var categories = mutableListOf<String>("all")

        categSet = databaseMenu.map{ it.category }.toSet()

        categories.addAll(categSet)

        var selectedCats by remember{ mutableStateOf("")}


        LazyRow (horizontalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.padding(start = 10.dp)){
            items(categories) { category ->
                Button(
                    onClick = {
                        if(category == "all"){
                            selectedCats = ""
                        }
                        else if( category == selectedCats) {
                            selectedCats = ""
                        }
                        else{
                            selectedCats = category
                        }
                              },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (category == "all" && selectedCats ==""){
                            LittleLemonColor.green
                        }
                        else if( category == selectedCats) {
                            LittleLemonColor.green
                        }
                        else{
                            LittleLemonColor.cloud
                        }
                    ),
                    shape = RoundedCornerShape(10.dp),
//                    modifier = Modifier.padding(start = 11.dp)
                )
                {
                    Text(
                        text = category.replaceFirstChar{it.uppercase()},
                        color =
                        if (category == "all" && selectedCats ==""){
                            LittleLemonColor.cloud
                        }
                        else if( category == selectedCats) {
                            LittleLemonColor.cloud
                        }
                        else{
                            LittleLemonColor.green
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        menuItems = databaseMenu.filter { menuItem ->
            (selectedCats.isEmpty() || menuItem.category == selectedCats)
                    &&
            (menuItem.title.lowercase().contains(input.lowercase()))
        }


        LazyColumn() {
            items(menuItems) { item ->
                dishDisplay(item = item, navcon)
            }
        }


    }

}



@Composable
fun HeroSection(){
    Column(
        Modifier
            .background(color = Color(0xFF495E57))
            .padding(10.dp)
            .fillMaxWidth()
            ) {
        Text(text = "Little Lemon",
            fontWeight = FontWeight.Bold,
            fontSize = 64.sp,
            color = Color(0xFFF4CE14),
            fontFamily = markazi
            )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.fillMaxWidth(0.6f)) {
                Text(text = "Chicago", color = Color(0xFFFFFFFF), fontSize = 40.sp, fontFamily = markazi , modifier =  Modifier)
                Text(text ="We are a family-owned Mediterranean restaurant, focused on traditional recipes served with a modern twist", fontSize = 18.sp, fontFamily = karla, color = Color(0xFFFFFFFF), modifier = Modifier.padding(bottom = 15.dp))
            }
           Image(
               painter = painterResource(id = R.drawable.hero_image),
               contentDescription ="hero image",
               modifier = Modifier
                   .clip(shape = RoundedCornerShape(40.dp))
                   .size(150.dp)

               ,
               contentScale = ContentScale.Crop
           )
        }
    }
}

@Composable
fun dishDisplay(item : MenuItem, navcon: NavHostController){
    Card(Modifier.clickable { navcon.navigate(Dish.r + "/${item.id}")}) {
        Card() {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                Column {
                    Text(
                        text = item.title ,
                        fontSize =18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LittleLemonColor.charcoal,
                        fontFamily = karla)
                    Text(text = item.desc,
                        color = LittleLemonColor.charcoal,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth(.6f)
                            .padding(vertical = 5.dp),
                        fontFamily = karla)
                    Text(
                        text = "$${item.price}",
                        color = LittleLemonColor.charcoal,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = karla
                    )
                }
                GlideImage(model = item.image, contentDescription = item.desc, modifier = Modifier
                    .padding(start = 40.dp)
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

            }

        }

    }
    Divider(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        thickness = 1.dp,
        color = Color(0xFFF4CE14)
    )
}
