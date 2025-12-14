package com.example.diario_de_classe2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diario_de_classe2.data.Aluno
import com.example.diario_de_classe2.data.DataSource
import com.example.diario_de_classe2.navigation.AppNavigation
import com.example.diario_de_classe2.ui.theme.Diario_de_ClasseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Diario_de_ClasseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun StudentCardButton(
    moreed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.moreed_more),
            tint = MaterialTheme.colorScheme.secondary
        )
    }

}


@Composable
fun CardStudent(
    modifier: Modifier,
    @DrawableRes img: Int,
    imgDescription: String,
    nameStudent: String,
    nameCourse: String
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            ),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(bottomStart = 30.dp, topEnd = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                painter = painterResource(img),
                contentDescription = imgDescription,
                contentScale = ContentScale.Crop
            )
            Column {
                Text(nameStudent, style = MaterialTheme.typography.displayMedium)
                Text(nameCourse, style = MaterialTheme.typography.labelSmall)
            }
            StudentCardButton(
                moreed = expanded,
                onClick = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .weight(1f)
                    .wrapContentSize(align = Alignment.CenterEnd)
            )
        }

        if (expanded) {
            MoreInfoStudent()
        }
    }
}

@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Diário de Classe",
            style = MaterialTheme.typography.displayLarge,
//            color = Color.White
        )
    }
}


@Composable
fun ListOfStudents(
    modifier: Modifier,
    AlunoList: List<Aluno>
) {
    LazyColumn(modifier = modifier) {
        items(AlunoList) { student ->
            CardStudent(
                modifier = Modifier.padding(8.dp),
                img = student.photo,
                imgDescription = "Foto do aluno",
                nameStudent = student.name,
                nameCourse = student.course
            )
        }
    }
}

@Composable
fun MoreInfoStudent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Email: aluno@gmail.com",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Telefone: (00) 0 0000-0000",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Endereço: Rua dos Alunos, 123 - Cidade, Estado",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PageResult(modifier: Modifier = Modifier) {
    val layoutDirection = LocalLayoutDirection.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(layoutDirection),
                end = WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(layoutDirection)
            )
    ) {

        ListOfStudents(
            modifier = modifier,
            studentsList = DataSource.loadAluno()
        )

    }
        CardStudent(
        modifier = Modifier.padding(8.dp),
       img = R.drawable.galeria_de_fotos,
            imgDescription = "Foto do aluno",
      nameStudent = "aluno",
       nameCourse = "Desenvolvimento de Sistemas"
    )

}