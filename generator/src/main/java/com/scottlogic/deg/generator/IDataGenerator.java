package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.outputs.TestCaseGenerationResult;

public interface IDataGenerator {
    TestCaseGenerationResult generateData(Profile profile, IAnalysedProfile analysedProfile);
}
