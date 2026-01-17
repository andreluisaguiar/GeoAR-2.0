# GeoAR - Jogo Educativo de Geometria em Realidade Aumentada

Aplicativo móvel educativo nativo para Android que utiliza realidade aumentada com ARCore para ensinar geometria de forma interativa e imersiva.

## 🎯 Características

- **Realidade Aumentada**: Experiências AR imersivas usando ARCore
- **Geometria Interativa**: Visualize e interaja com formas geométricas 3D no espaço real
- **Conteúdo Educativo**: Módulos básicos, intermediários e avançados sobre geometria
- **Progressão de Aprendizado**: Sistema de progresso e conquistas
- **Interface Moderna**: UI construída com Jetpack Compose

## 📱 Requisitos

- Android 7.0 (API 24) ou superior
- ARCore instalado e suportado
- Dispositivo com câmera e sensores de movimento

## 🏗️ Arquitetura

```
app/
├── ui/           # Activities, Fragments, ViewModels, UI (Compose)
├── ar/           # ARCore session management
├── rendering/    # Renderização 3D (OpenGL ES)
├── geometry/     # Lógica de formas geométricas
├── education/    # Conteúdo educativo
└── data/         # SharedPreferences, armazenamento local
```

## 🔧 Tecnologias Utilizadas

- **Kotlin**: Linguagem principal
- **ARCore SDK**: SDK nativo para realidade aumentada
- **OpenGL ES**: Renderização de gráficos 3D
- **Jetpack Compose**: UI moderna e declarativa
- **SharedPreferences**: Armazenamento local

## 📦 Dependências Principais

```kotlin
implementation 'com.google.ar:core:1.40.0'
implementation 'androidx.compose:compose-bom:2023.10.01'
implementation 'androidx.opengl:opengl:1.0.0'
```

## 🎮 Funcionalidades

### Módulo Básico
- Cubos
- Esferas
- Cilindros
- Quadrados
- Círculos
- Triângulos

### Módulo Intermediário
- Cones
- Pirâmides
- Cálculos de área, volume e perímetro

### Módulo Avançado
- Teoremas geométricos interativos
- Visualizações avançadas

## 🚀 Como Construir

1. Clone o repositório
2. Abra o projeto no Android Studio
3. Sincronize as dependências do Gradle
4. Conecte um dispositivo compatível ou use um emulador com ARCore
5. Build e execute o projeto

## 📚 Documentação

### Configuração ARCore

O app verifica automaticamente a compatibilidade com ARCore:

```kotlin
val availability = sessionManager.checkAvailability()
when (availability) {
    ARSessionManager.ARCoreAvailability.INSTALLED -> {
        // ARCore pronto para uso
    }
    ARSessionManager.ARCoreAvailability.NOT_INSTALLED -> {
        // Solicitar instalação do ARCore
    }
    else -> {
        // Tratar outros casos
    }
}
```

### Posicionamento de Objetos

Toque na superfície detectada para posicionar objetos geométricos:

```kotlin
fun placeObjectOnSurface(event: MotionEvent) {
    val frame = session.update()
    val hitResults = frame.hitTest(event.x, event.y)
    // Posicionar objeto...
}
```

### Sistema de Progresso

O progresso do usuário é salvo localmente usando SharedPreferences:

```kotlin
val progress = progressRepository.loadProgress()
val updatedProgress = progress.incrementShapesPlaced()
progressRepository.saveProgress(updatedProgress)
```

## 🎨 Estrutura do Projeto

- **ARSessionManager**: Gerencia sessão ARCore
- **GeometryRenderer**: Renderiza formas 3D com OpenGL ES
- **GeometryCalculator**: Calcula propriedades geométricas
- **EducationalContentProvider**: Fornece conteúdo educativo
- **ProgressRepository**: Gerencia progresso do usuário

## 📖 Módulos Educativos

Cada forma geométrica inclui:
- Descrição detalhada
- Propriedades geométricas
- Fórmulas matemáticas
- Exemplos práticos

## 🏆 Conquistas

- Primeira forma colocada
- 10 formas colocadas
- Domínio de geometria básica
- Mestre em geometria 3D

## 📝 Licença

Este projeto é educacional e está disponível para fins de aprendizado.

## 👨‍💻 Desenvolvido com

- Android Studio
- Kotlin
- ARCore
- OpenGL ES
- Jetpack Compose

---

**Nota**: Este aplicativo requer um dispositivo Android compatível com ARCore para funcionar corretamente.

