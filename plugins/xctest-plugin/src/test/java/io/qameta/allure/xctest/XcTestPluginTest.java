/*
 *  Copyright 2019 Qameta Software OÜ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.qameta.allure.xctest;

import io.qameta.allure.context.JacksonContext;
import io.qameta.allure.core.Configuration;
import io.qameta.allure.core.ResultsVisitor;
import io.qameta.allure.entity.TestResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.TempDirectory;
import org.junitpioneer.jupiter.TempDirectory.TempDir;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author charlie (Dmitry Baev).
 */
@ExtendWith(TempDirectory.class)
class XcTestPluginTest {

    private Configuration configuration;
    private ResultsVisitor visitor;
    private Path resultsDirectory;

    @BeforeEach
    void setUp(@TempDir final Path resultsDirectory) {
        configuration = mock(Configuration.class);
        when(configuration.requireContext(JacksonContext.class)).thenReturn(new JacksonContext());
        visitor = mock(ResultsVisitor.class);
        this.resultsDirectory = resultsDirectory;
    }

    @Test
    void shouldParseResults() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("sample.plist")) {
            Files.copy(Objects.requireNonNull(is), resultsDirectory.resolve("sample.plist"));
        }

        new XcTestPlugin().readResults(configuration, visitor, resultsDirectory);

        verify(visitor, times(14))
                .visitTestResult(any(TestResult.class));
    }
}
