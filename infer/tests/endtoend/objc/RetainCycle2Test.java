/*
 * Copyright (c) 2013- Facebook.
 * All rights reserved.
 */

package endtoend.objc;

import static org.hamcrest.MatcherAssert.assertThat;
import static utils.matchers.ResultContainsExactly.containsExactly;
import static utils.matchers.ResultContainsNoErrorInMethod.doesNotContain;

import com.google.common.collect.ImmutableList;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.IOException;

import utils.DebuggableTemporaryFolder;
import utils.InferException;
import utils.InferResults;
import utils.InferRunner;

public class RetainCycle2Test {

  public static final String retain_cycle_file =
      "infer/tests/codetoanalyze/objc/errors/" +
          "memory_leaks_benchmark/retain_cycle2.m";

  private static ImmutableList<String> inferCmd;

  public static final String RETAIN_CYCLE = "RETAIN_CYCLE";

  @ClassRule
  public static DebuggableTemporaryFolder folder =
      new DebuggableTemporaryFolder();

  @BeforeClass
  public static void runInfer() throws InterruptedException, IOException {
    inferCmd = InferRunner.createiOSInferCommandWithMLBuckets(
        folder,
        retain_cycle_file,
        "cf",
        true);
  }



  @Test
  public void whenInferRunsOnStrongCycleThenRCIsFound()
      throws InterruptedException, IOException, InferException {
    InferResults inferResults = InferRunner.runInferObjC(inferCmd);
    assertThat(
        "Results should contain retain cycle",
        inferResults,
        containsExactly(
            RETAIN_CYCLE,
            retain_cycle_file,
            new String[]{"strongcycle"}));
  }


}
