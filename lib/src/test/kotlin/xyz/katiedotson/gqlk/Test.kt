package xyz.katiedotson.gqlk

import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.SelectClasses
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
@SelectClasses(CountriesRepositoryTest::class, SpacexRepositoryTest::class)
class Test