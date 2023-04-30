package net.meilcli.hubber.toolchain.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.internal.ActiveByDefault
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPublic

@ActiveByDefault(since = "1.20.0")
class InternalVisibility(config: Config) : Rule(config) {

    override val issue = Issue(
        "InternalVisibility",
        Severity.Warning,
        "Internal package can contains only non-public members",
        debt = Debt.FIVE_MINS
    )

    override fun visitClassOrObject(classOrObject: KtClassOrObject) {
        super.visitClassOrObject(classOrObject)

        if (classOrObject.isTopLevel().not()) {
            return
        }

        val packageName = classOrObject.containingKtFile
            .packageFqName
            .asString()
        if (packageName.contains(".internal.").not() && packageName.endsWith(".internal").not()) {
            return
        }

        if (classOrObject.isPublic) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(classOrObject),
                    message = "${classOrObject.name} class/object must not be public in $packageName"
                )
            )
        }
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)

        if (property.isTopLevel.not()) {
            return
        }

        val packageName = property.containingKtFile
            .packageFqName
            .asString()
        if (packageName.contains(".internal.").not() && packageName.endsWith(".internal").not()) {
            return
        }

        if (property.isPublic) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(property),
                    message = "${property.name} property must not be public in $packageName"
                )
            )
        }
    }

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        if (function.isTopLevel.not()) {
            return
        }

        val packageName = function.containingKtFile
            .packageFqName
            .asString()
        if (packageName.contains(".internal.").not() && packageName.endsWith(".internal").not()) {
            return
        }

        if (function.isPublic) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(function),
                    message = "${function.name} function must not be public in $packageName"
                )
            )
        }
    }
}
