package com.betomorrow.gradle.wording.domain

import java.nio.file.Paths
import java.util.Properties

class PropertiesUpdater(val path: String) {

    /**
     * Update file with wording and returns updated keys
     */
    fun update(wording: Map<String, String>, addMissingWording: Boolean): Set<String> {

        val outputKeys = HashSet<String>()

        val properties = loadOrCreateProperties(Paths.get(path))

        updateStrings(properties, wording, outputKeys)

        val missingWordings = wording.keys - outputKeys
        if (addMissingWording) {
            addMissingStrings(
                properties,
                missingWordings,
                wording,
            )

            outputKeys.addAll(wording.keys)
        }

        writeToFile(properties, path)

        return outputKeys
    }

    private fun updateStrings(properties: Properties, wording: Map<String, String>, outputKeys: HashSet<String>) {
        properties.stringPropertyNames().forEach { key ->
            if (wording.containsKey(key)) {
                properties.setProperty(key, wording[key])
                outputKeys.add(key)
            }
        }
    }

    private fun addMissingStrings(properties: Properties, keys: Set<String>, wording: Map<String, String>) {
        keys.forEach { key ->
            properties.setProperty(key, wording[key])
        }
    }
}