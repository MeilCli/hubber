package net.meilcli.hubber.toolchain.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import io.gitlab.arturbosch.detekt.api.internal.ActiveByDefault
import io.gitlab.arturbosch.detekt.api.internal.Configuration
import org.jetbrains.kotlin.psi.KtClass

@ActiveByDefault(since = "1.20.0")
class InterfaceName(config: Config) : Rule(config) {

    @Configuration("interface regex pattern")
    private val interfacePattern by config("^I[A-Z][A-Za-z0-9]*") { Regex(it) }

    override val issue = Issue(
        "InterfaceNaming",
        Severity.Style,
        "A interface's name must fit the naming pattern defined in the projects configurations",
        debt = Debt.FIVE_MINS
    )

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)

        if (klass.isInterface().not()) {
            return
        }

        if (klass.name?.matches(interfacePattern) != true) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(klass),
                    message = "interface ${klass.name} must match the pattern: $interfacePattern"
                )
            )
        }
    }
}
