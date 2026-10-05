package org.example.eshopnew;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(
        packages = "org.example.eshopnew",
        importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    // --- Naming / annotation conventions -------------------------------------

    @ArchTest
    static final ArchRule controllers_are_annotated_and_suffixed =
            classes().that().areAnnotatedWith(RestController.class)
                    .should().haveSimpleNameEndingWith("Controller")
                    .as("@RestController classes should be named *Controller");

    @ArchTest
    static final ArchRule controllers_named_so_are_rest_controllers =
            classes().that().haveSimpleNameEndingWith("Controller")
                    .should().beAnnotatedWith(RestController.class)
                    .as("*Controller classes should be annotated with @RestController");

    @ArchTest
    static final ArchRule services_are_annotated_and_suffixed =
            classes().that().areAnnotatedWith(Service.class)
                    .should().haveSimpleNameEndingWith("Service")
                    .as("@Service classes should be named *Service");

    @ArchTest
    static final ArchRule repositories_are_interfaces =
            classes().that().haveSimpleNameEndingWith("Repository")
                    .should().beInterfaces()
                    .andShould().beAssignableTo(Repository.class)
                    .as("*Repository types should be Spring Data repository interfaces");

    // --- Layering (enforced by class role, since code is package-by-feature) --

    @ArchTest
    static final ArchRule nothing_depends_on_controllers =
            classes().that().haveSimpleNameEndingWith("Controller")
                    .should().onlyHaveDependentClassesThat().haveSimpleNameEndingWith("Controller")
                    .as("Controllers are entry points and should not be depended on by other classes");

    @ArchTest
    static final ArchRule repositories_are_used_only_by_services =
            classes().that().haveSimpleNameEndingWith("Repository")
                    .should().onlyHaveDependentClassesThat(
                            DescribedPredicate.describe(
                                    "are Services or Repositories",
                                    javaClass -> javaClass.getSimpleName().endsWith("Service")
                                            || javaClass.getSimpleName().endsWith("Repository")))
                    .as("Repositories should be accessed only by services");

    @ArchTest
    static final ArchRule services_do_not_depend_on_controllers =
            noClasses().that().haveSimpleNameEndingWith("Service")
                    .should().dependOnClassesThat().haveSimpleNameEndingWith("Controller")
                    .as("Services should not depend on controllers");

    // --- General hygiene ------------------------------------------------------

    // Library shorthand: covers @Autowired/@Value/@Inject/@Resource on fields in one rule.
    @ArchTest
    static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    // Explicit, per-annotation equivalents of the rule above.
    @ArchTest
    static final ArchRule fields_are_not_autowired =
            fields().should().notBeAnnotatedWith(Autowired.class)
                    .as("Fields should not be @Autowired; use constructor injection");

    @ArchTest
    static final ArchRule fields_are_not_value_injected =
            fields().should().notBeAnnotatedWith(Value.class)
                    .as("Fields should not be injected with @Value; inject via the constructor");

    @ArchTest
    static final ArchRule fields_are_not_inject_annotated =
            fields().should().notBeAnnotatedWith(Inject.class)
                    .as("Fields should not use @Inject; use constructor injection");

    @ArchTest
    static final ArchRule fields_are_not_resource_annotated =
            fields().should().notBeAnnotatedWith(Resource.class)
                    .as("Fields should not use @Resource; use constructor injection");

    @ArchTest
    static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

    @ArchTest
    static final ArchRule feature_slices_are_free_of_cycles =
            slices().matching("org.example.eshopnew.(*)..")
                    .should().beFreeOfCycles();
}
