package net.meilcli.hubber.toolchain.dokka

import net.meilcli.hubber.toolchain.dokka.transformers.NoDocumentedFilter
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement

class HideNoDocumentedPlugin : DokkaPlugin() {

    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val suppressByNoDocumentedFilter by extending {
        dokkaBase.preMergeDocumentableTransformer providing ::NoDocumentedFilter order { before(dokkaBase.emptyPackagesFilter) }
    }

    @OptIn(DokkaPluginApiPreview::class)
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement {
        return PluginApiPreviewAcknowledgement
    }
}
