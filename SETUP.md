# Guia de Configuração - GeoAR

## Pré-requisitos

### 1. Android Studio
- Android Studio Hedgehog (2023.1.1) ou superior
- Android SDK Platform 34
- Android SDK Build-Tools 34.0.0

### 2. Dispositivo Compatível
Para testar o aplicativo, você precisará de um dispositivo Android que:
- Execute Android 7.0 (API 24) ou superior
- Seja compatível com ARCore
- Tenha câmera e sensores de movimento

**Dispositivos ARCore compatíveis incluem:**
- Samsung Galaxy S8 e superiores
- Google Pixel 3 e superiores
- OnePlus 6 e superiores
- Entre outros (consulte [lista completa](https://developers.google.com/ar/discover/supported-devices))

## Instalação e Configuração

### Passo 1: Clonar e Abrir o Projeto

```bash
git clone <repository-url>
cd GeoAR
```

Abra o projeto no Android Studio.

### Passo 2: Sincronizar Dependências

1. Abra o projeto no Android Studio
2. Aguarde a sincronização automática do Gradle
3. Se houver erros, vá em `File > Sync Project with Gradle Files`

### Passo 3: Configurar ARCore

O ARCore será instalado automaticamente quando você executar o app pela primeira vez em um dispositivo compatível. Alternativamente:

```bash
# Via ADB
adb install -r ./arcore-android-sdk/samples/hello_ar_c/app/build/outputs/apk/debug/hello_ar_c-debug.apk
```

### Passo 4: Build e Executar

1. Conecte seu dispositivo via USB
2. Habilite **Depuração USB** nas configurações do desenvolvedor
3. Clique em `Run` ou pressione `Shift + F10`

## Estrutura do Projeto

```
GeoAR/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/geoar/app/
│   │       │   ├── ui/           # Activities e UI (Compose)
│   │       │   ├── ar/           # Gerenciamento ARCore
│   │       │   ├── rendering/    # Renderização 3D
│   │       │   ├── geometry/     # Lógica geométrica
│   │       │   ├── education/   # Conteúdo educativo
│   │       │   └── data/         # Armazenamento local
│   │       ├── res/              # Recursos (layouts, strings, etc)
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Dependências

O projeto usa as seguintes dependências principais:

- **ARCore**: `com.google.ar:core:1.40.0`
- **Compose**: `androidx.compose:compose-bom:2023.10.01`
- **OpenGL**: `androidx.opengl:opengl:1.0.0`
- **Material**: `com.google.android.material:material:1.10.0`

## Funcionalidades Implementadas

✅ ARCore Session Management
✅ Plane Detection (Detecção de superfícies)
✅ Anchor System (Posicionamento de objetos)
✅ OpenGL ES Rendering (Renderização 3D)
✅ Módulo de Geometria Básica
✅ Sistema de Progresso Local
✅ UI com Jetpack Compose
✅ Gestos e Interações (Touch)

## Como Usar

### 1. Abrir o App

Execute o aplicativo e você verá:
- Tela de boas-vindas
- Status do ARCore
- Seleção de módulo (Básico, Intermediário, Avançado)
- Grid de formas geométricas

### 2. Selecionar uma Forma

1. Escolha um módulo (Básico, Intermediário ou Avançado)
2. Toque em uma forma geométrica para selecioná-la
3. Você será redirecionado para a tela AR

### 3. Usar AR

1. Aponte a câmera para uma superfície plana
2. Aguarde a detecção (aparecerá um indicador)
3. Toque na tela para posicionar a forma
4. Use os botões para:
   - Ver informações educativas
   - Deletar objetos selecionados
   - Limpar toda a cena

## Resolução de Problemas

### ARCore não está disponível

**Problema**: "ARCore não está disponível neste dispositivo"

**Solução**: 
- Verifique se seu dispositivo é compatível com ARCore
- Instale o ARCore via Play Store
- Verifique se a câmera está funcionando

### Erro ao compilar

**Problema**: Erros de compilação

**Solução**:
1. Limpe o projeto: `Build > Clean Project`
2. Reconstrua: `Build > Rebuild Project`
3. Invalide caches: `File > Invalidate Caches / Restart`

### Dependências não sincronizam

**Problema**: Gradle não sincroniza dependências

**Solução**:
1. Verifique sua conexão com a internet
2. Execute `./gradlew --refresh-dependencies`
3. Delete `.gradle` folder e sincronize novamente

## Desenvolvimento

### Adicionar Nova Forma Geométrica

1. Adicione o tipo em `geometry/GeometryType.kt`
2. Crie os dados de vértices em `rendering/GeometryRenderer.kt`
3. Adicione conteúdo educativo em `education/EducationalContent.kt`

### Modificar Conteúdo Educativo

Edite o arquivo `education/EducationalContent.kt` para adicionar ou modificar o conteúdo educativo de cada forma.

### Personalizar Cores

Edite `res/values/colors.xml` para personalizar as cores do app.

## Build de Release

Para gerar um APK de release:

```bash
./gradlew assembleRelease
```

O APK será gerado em: `app/build/outputs/apk/release/`

## Testes

Execute os testes:

```bash
./gradlew test
```

## Contribuindo

Este é um projeto educacional. Sinta-se livre para:
- Reportar bugs
- Sugerir melhorias
- Adicionar novas formas geométricas
- Melhorar o conteúdo educativo

## Suporte

Para dúvidas ou problemas:
- Consulte a [documentação do ARCore](https://developers.google.com/ar/develop)
- Verifique os logs: `adb logcat | grep GeoAR`

---

**Desenvolvido com** ❤️ **usando Kotlin, ARCore e Jetpack Compose**

