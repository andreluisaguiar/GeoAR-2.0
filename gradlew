#!/bin/sh

# GeoAR - Gradle Wrapper Script
# This script is simplified. For full functionality, use Android Studio.

APP_HOME="$(cd -P -- "$(dirname -- "$0")" >/dev/null && pwd -P)"

echo "═══════════════════════════════════════"
echo "  GeoAR - Gradle Wrapper"
echo "═══════════════════════════════════════"
echo ""
echo "⚠️  IMPORTANTE: Use Android Studio para build completo"
echo ""
echo "Para testar a aplicação:"
echo "1. Abra o projeto no Android Studio"
echo "2. Aguarde a sincronização do Gradle"
echo "3. Conecte um dispositivo ARCore compatível"
echo "4. Clique em 'Run'"
echo ""
echo "═══════════════════════════════════════"

# Verificar se java está disponível
if command -v java &> /dev/null; then
    echo "✅ Java encontrado: $(java -version 2>&1 | head -n 1)"
else
    echo "❌ Java não encontrado"
    exit 1
fi

# Verificar se o wrapper jar existe
if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "❌ gradle-wrapper.jar não encontrado"
    echo "   Baixe de: https://services.gradle.org/distributions/gradle-wrapper.jar"
    exit 1
fi

echo ""
echo "✅ Gradle wrapper configurado"
echo ""
echo "Para instalação manual do Gradle:"
echo "  sudo apt install gradle  # Linux"
echo ""
echo "Ou use Android Studio (Recomendado)"
