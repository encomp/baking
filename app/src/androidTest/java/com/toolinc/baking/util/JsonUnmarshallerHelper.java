package com.toolinc.baking.util;

import android.content.Context;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.toolinc.baking.client.model.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.test.platform.app.InstrumentationRegistry;

/** Helper class to unrmashall json files. */
public final class JsonUnmarshallerHelper {

  public static final Recipe toRecipe(String fileName) {
    try {
      Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
      InputStream is = ctx.getResources().getAssets().open(fileName);
      return Recipe.builder().fromJson(readTextStream(is));
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to unmarshall to a Json object.");
    }
  }

  private static String readTextStream(InputStream inputStream) throws IOException {
    return CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
  }
}
