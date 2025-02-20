/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.material.composethemeadapter

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.test.filters.MediumTest
import com.google.android.material.composethemeadapter.test.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@MediumTest
@RunWith(Parameterized::class)
class MdcThemeTest<T : AppCompatActivity>(activityClass: Class<T>) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun activities() = listOf(DarkMdcActivity::class.java, LightMdcActivity::class.java)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule(activityClass)

    @Test
    fun colors() = composeTestRule.setContent {
        MdcTheme {
            val color = MaterialTheme.colors

            assertEquals(colorResource(R.color.aquamarine), color.primary)
            assertEquals(colorResource(R.color.royal_blue), color.primaryVariant)
            assertEquals(colorResource(R.color.midnight_blue), color.onPrimary)

            assertEquals(colorResource(R.color.dark_golden_rod), color.secondary)
            assertEquals(colorResource(R.color.slate_gray), color.onSecondary)
            assertEquals(colorResource(R.color.blue_violet), color.secondaryVariant)

            assertEquals(colorResource(R.color.spring_green), color.surface)
            assertEquals(colorResource(R.color.navy), color.onSurface)

            assertEquals(colorResource(R.color.dark_salmon), color.error)
            assertEquals(colorResource(R.color.beige), color.onError)

            assertEquals(colorResource(R.color.light_coral), color.background)
            assertEquals(colorResource(R.color.orchid), color.onBackground)

            // MdcTheme updates the LocalContentColor to match the calculated onBackground
            assertEquals(colorResource(R.color.orchid), LocalContentColor.current)
        }
    }

    @Test
    fun shapes() = composeTestRule.setContent {
        MdcTheme {
            val shapes = MaterialTheme.shapes
            val density = LocalDensity.current

            shapes.small.run {
                assertTrue(this is CutCornerShape)
                assertEquals(4f, topStart.toPx(density))
                assertEquals(9.dp.scaleToPx(density), topEnd.toPx(density))
                assertEquals(5f, bottomEnd.toPx(density))
                assertEquals(3.dp.scaleToPx(density), bottomStart.toPx(density))
            }
            shapes.medium.run {
                assertTrue(this is RoundedCornerShape)
                assertEquals(12.dp.scaleToPx(density), topStart.toPx(density))
                assertEquals(12.dp.scaleToPx(density), topEnd.toPx(density))
                assertEquals(12.dp.scaleToPx(density), bottomEnd.toPx(density))
                assertEquals(12.dp.scaleToPx(density), bottomStart.toPx(density))
            }
            shapes.large.run {
                assertTrue(this is CutCornerShape)
                assertEquals(0f, topStart.toPx(density))
                assertEquals(0f, topEnd.toPx(density))
                assertEquals(0f, bottomEnd.toPx(density))
                assertEquals(0f, bottomStart.toPx(density))
            }
        }
    }

    @Test
    fun type() = composeTestRule.setContent {
        MdcTheme {
            val typography = MaterialTheme.typography
            val density = LocalDensity.current

            val rubik = FontFamily(
                Font(resId = R.font.rubik_300, weight = FontWeight.W300),
                Font(resId = R.font.rubik_400, weight = FontWeight.W400),
                Font(resId = R.font.rubik_500, weight = FontWeight.W500),
                Font(resId = R.font.rubik_700, weight = FontWeight.W700)
            )
            val rubik300 = Font(R.font.rubik_300).toFontFamily()
            val rubik400 = Font(R.font.rubik_400).toFontFamily()
            val sansSerif = FontFamilyWithWeight(FontFamily.SansSerif)
            val sansSerifLight = FontFamilyWithWeight(FontFamily.SansSerif, FontWeight.Light)
            val sansSerifBlack = FontFamilyWithWeight(FontFamily.SansSerif, FontWeight.Black)
            val serif = FontFamilyWithWeight(FontFamily.Serif)
            val cursive = FontFamilyWithWeight(FontFamily.Cursive)
            val monospace = FontFamilyWithWeight(FontFamily.Monospace)

            typography.h1.run {
                assertTextUnitEquals(97.54.sp, fontSize, density)
                assertTextUnitEquals((-0.0015).em, letterSpacing, density)
                assertEquals(rubik300, fontFamily)
            }

            assertNotNull(typography.h2.shadow)
            typography.h2.shadow!!.run {
                assertEquals(colorResource(R.color.olive_drab), color)
                assertEquals(4.43f, offset.x)
                assertEquals(8.19f, offset.y)
                assertEquals(2.13f, blurRadius)
            }

            typography.h3.run {
                assertEquals(sansSerif.fontFamily, fontFamily)
                assertEquals(sansSerif.weight, fontWeight)
            }

            typography.h4.run {
                assertEquals(sansSerifLight.fontFamily, fontFamily)
                assertEquals(sansSerifLight.weight, fontWeight)
            }

            typography.h5.run {
                assertEquals(sansSerifBlack.fontFamily, fontFamily)
                assertEquals(sansSerifBlack.weight, fontWeight)
            }

            typography.h6.run {
                assertEquals(serif.fontFamily, fontFamily)
                assertEquals(serif.weight, fontWeight)
            }

            typography.body1.run {
                assertTextUnitEquals(16.26.sp, fontSize, density)
                assertTextUnitEquals(0.005.em, letterSpacing, density)
                assertEquals(rubik400, fontFamily)
                assertNull(shadow)
            }

            typography.body2.run {
                assertEquals(cursive.fontFamily, fontFamily)
                assertEquals(cursive.weight, fontWeight)
            }

            typography.subtitle1.run {
                assertEquals(monospace.fontFamily, fontFamily)
                assertEquals(monospace.weight, fontWeight)
                assertTextUnitEquals(0.em, letterSpacing, density)
            }

            typography.subtitle2.run {
                assertEquals(FontFamily.SansSerif, fontFamily)
            }

            typography.button.run {
                assertEquals(rubik, fontFamily)
            }

            typography.caption.run {
                assertEquals(FontFamily.SansSerif, fontFamily)
                assertTextUnitEquals(0.04.em, letterSpacing, density)
            }

            typography.overline.run {
                assertEquals(FontFamily.SansSerif, fontFamily)
            }
        }
    }
}

private fun Dp.scaleToPx(density: Density): Float {
    val dp = this
    return with(density) { dp.toPx() }
}

private fun assertTextUnitEquals(expected: TextUnit, actual: TextUnit, density: Density) {
    if (expected.javaClass == actual.javaClass) {
        // If the expected and actual are the same type, compare the raw values with a
        // delta to account for float inaccuracy
        assertEquals(expected.value, actual.value, 0.001f)
    } else {
        // Otherwise we need to flatten to a px to compare the values. Again using a
        // delta to account for float inaccuracy
        with(density) { assertEquals(expected.toPx(), actual.toPx(), 0.001f) }
    }
}

private fun CornerSize.toPx(density: Density) = toPx(Size.Unspecified, density)
