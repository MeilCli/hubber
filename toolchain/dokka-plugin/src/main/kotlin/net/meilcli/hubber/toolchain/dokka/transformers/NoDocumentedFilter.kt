package net.meilcli.hubber.toolchain.dokka.transformers

import org.jetbrains.dokka.base.transformers.documentables.SuppressedByConditionDocumentableFilterTransformer
import org.jetbrains.dokka.model.DClasslike
import org.jetbrains.dokka.model.DEnumEntry
import org.jetbrains.dokka.model.DFunction
import org.jetbrains.dokka.model.DPackage
import org.jetbrains.dokka.model.DProperty
import org.jetbrains.dokka.model.DTypeAlias
import org.jetbrains.dokka.model.Documentable
import org.jetbrains.dokka.model.dfs
import org.jetbrains.dokka.plugability.DokkaContext

class NoDocumentedFilter(dokkaContext: DokkaContext) : SuppressedByConditionDocumentableFilterTransformer(dokkaContext) {

    override fun shouldBeSuppressed(d: Documentable): Boolean {
        return when (d) {
            is DFunction -> d.documentation.isEmpty()
            is DProperty -> d.documentation.isEmpty()
            is DTypeAlias -> d.documentation.isEmpty()
            is DEnumEntry -> d.documentation.isEmpty()
            is DPackage -> d.dfs { it.documentation.isNotEmpty() } == null
            is DClasslike -> d.dfs { it.documentation.isNotEmpty() } == null
            else -> d.documentation.isEmpty()
        }
    }
}
