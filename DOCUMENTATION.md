# Documentação Técnica - GeoAR

## Visão Geral

O GeoAR é um aplicativo educacional Android que utiliza Realidade Aumentada (AR) para ensinar geometria de forma interativa e imersiva. O projeto foi desenvolvido usando Kotlin, ARCore e OpenGL ES para renderização 3D.

## Arquitetura

### Estrutura Modular

```
app/src/main/java/com/geoar/app/
├── ui/               # Camada de Interface
│   ├── MainActivity.kt              # Tela principal (Jetpack Compose)
│   ├── ar/
│   │   ├── GeometryARActivity.kt    # Activity AR
│   │   ├── ARViewModel.kt          # ViewModel para AR
│   │   └── ARViewModelFactory.kt   # Factory do ViewModel
│   └── theme/                       # Tema e cores
├── ar/               # Camada ARCore
│   ├── ARSessionManager.kt         # Gerenciamento de sessão ARCore
│   └── ARHitTest.kt               # Testes de hit em AR
├── rendering/         # Camada de Renderização
│   └── GeometryRenderer.kt         # Renderização OpenGL ES
├── geometry/          # Camada de Geometria
│   ├── GeometryType.kt             # Enum de formas geométricas
│   └── GeometryCalculator.kt       # Cálculos geométricos
├── education/         # Camada Educativa
│   └── EducationalContent.kt       # Conteúdo educativo
└── data/             # Camada de Dados
    ├── UserProgress.kt              # Modelo de progresso
    └── ProgressRepository.kt       # Repositório de dados
```

## Componentes Principais

### 1. ARSessionManager

Gerencia a sessão ARCore e verifica a disponibilidade.

```kotlin
class ARSessionManager(private val context: Context) {
    fun checkAvailability(): ARCoreAvailability
    suspend fun initializeSession(): Session
    fun getDetectedPlanes(): List<Plane>
    fun createAnchor(pose: Pose): Anchor?
}
```

**Responsabilidades:**
- Verificar compatibilidade ARCore
- Inicializar sessão ARCore
- Gerenciar ciclo de vida da sessão
- Detectar planos horizontais

### 2. GeometryRenderer

Renderiza formas 3D usando OpenGL ES.

```kotlin
class GeometryRenderer {
    fun renderGeometry(type: GeometryType, position: FloatArray, 
                       scale: Float, color: FloatArray)
    fun setProjectionMatrix(matrix: FloatArray)
    fun setViewMatrix(matrix: FloatArray)
}
```

**Capacidades:**
- Renderização de 8 tipos de formas geométricas
- Suporte a cores personalizadas
- Transformações (posição, escala, rotação)
- Shaders OpenGL ES 2.0

### 3. GeometryCalculator

Calcula propriedades geométricas.

```kotlin
object GeometryCalculator {
    fun calculateVolume(type: GeometryType, vararg dimensions: Float): Float
    fun calculateArea(type: GeometryType, vararg dimensions: Float): Float
    fun calculatePerimeter(type: GeometryType, vararg dimensions: Float): Float
}
```

### 4. EducationalContentProvider

Fornece conteúdo educativo para cada forma.

```kotlin
object EducationalContentProvider {
    fun getContentFor(geometryType: GeometryType): EducationalContent
}
```

**Dados incluídos:**
- Título e descrição
- Propriedades geométricas
- Fórmulas matemáticas
- Exemplos práticos

### 5. ProgressRepository

Gerencia o progresso do usuário localmente.

```kotlin
class ProgressRepository(context: Context) {
    fun saveProgress(progress: UserProgress)
    fun loadProgress(): UserProgress
    fun clearProgress()
}
```

**Armazenamento:**
- Usa SharedPreferences
- Serialização JSON com kotlinx.serialization
- Persistência entre sessões

## Fluxo de Dados

### Inicialização do App

```
MainActivity
    ├── Verifica status ARCore
    ├── Carrega progresso do usuário
    ├── Exibe menu de formas geométricas
    └── Abre GeometryARActivity ao selecionar forma
```

### Sessão AR

```
GeometryARActivity
    ├── Inicializa ARCore Session
    ├── Configura GLSurfaceView
    ├── Detecta planos horizontais
    ├── Captura toque do usuário
    ├── Realiza hit test
    ├── Cria anchor
    ├── Renderiza forma 3D
    └── Atualiza progresso
```

## Sistema de Gestos

### Touch Events

```kotlin
override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
        placeObjectOnSurface(event)
    }
    return super.onTouchEvent(event)
}
```

**Eventos suportados:**
- `ACTION_DOWN`: Toque na superfície para posicionar objeto
- Plane detection: Detecta superfícies horizontais
- Hit test: Encontra posição no mundo 3D

## Renderização 3D

### Pipeline OpenGL ES

```
Vertex Shader
    ↓
Geometry Processing
    ↓
Fragment Shader
    ↓
Framebuffer
```

**Shader Personalizado:**
- Vertex shader: Transformações de posição
- Fragment shader: Cor por vértice
- MVP Matrix: Model-View-Projection

### Geometrias Suportadas

| Forma | Vértices | Tipo |
|-------|----------|------|
| Cubo | 24 | 3D |
| Esfera | ~400 | 3D |
| Cilindro | ~40 | 3D |
| Cone | ~20 | 3D |
| Pirâmide | 12 | 3D |
| Quadrado | 4 | 2D |
| Círculo | 32 | 2D |
| Triângulo | 3 | 2D |

## Módulos Educativos

### Módulo Básico
- **Foco**: Formas básicas e propriedades fundamentais
- **Formas**: Cubo, Esfera, Cilindro, Quadrado, Círculo, Triângulo
- **Conceitos**: Área, Perímetro, Volume básico

### Módulo Intermediário
- **Foco**: Formas complexas e cálculos avançados
- **Formas**: Cone, Pirâmide
- **Conceitos**: Volume complexo, área lateral, proporções

### Módulo Avançado
- **Foco**: Teoremas e visualizações complexas
- **Status**: Em desenvolvimento

## Performance

### Otimizações Implementadas

1. **ARCore Session Management**
   - Resume/Pause automático
   - Cleanup de recursos
   - Gerenciamento de memória

2. **OpenGL ES**
   - Vertex buffers reutilizados
   - Shaders compilados uma vez
   - Depth testing habilitado

3. **Memória**
   - SharedPreferences para dados pequenos
   - Cleanup de anchors não utilizadas
   - Otimização de meshes 3D

### Métricas Esperadas

- **FPS**: 60 FPS em dispositivos compatíveis
- **Latência**: <16ms por frame
- **Uso de memória**: <100MB para cena simples
- **Bateria**: ~2% por minuto de uso AR

## Dependências

### Core
```
com.google.ar:core:1.40.0
androidx.compose:compose-bom:2023.10.01
androidx.opengl:opengl:1.0.0
```

### UI
```
androidx.compose.material3:material3
androidx.activity:activity-compose
androidx.navigation:navigation-compose
```

### Data
```
org.jetbrains.kotlinx:kotlinx-serialization-json
```

## Limitações Conhecidas

1. **Dispositivos Compatíveis**
   - Requer dispositivo com ARCore
   - Não funciona em emuladores padrão

2. **Ambiente**
   - Funciona melhor com boa iluminação
   - Superfícies planas são necessárias

3. **Performance**
   - Dispositivos mais antigos podem ter FPS reduzido
   - Sessões longas podem consumir bateria

## Extensibilidade

### Adicionar Nova Forma

1. **GeometryType.kt**
```kotlin
NOVA_FORMA(
    "Nome", 
    ModuleType.X, 
    "Descrição", 
    "Fórmula"
)
```

2. **GeometryRenderer.kt**
```kotlin
private val novaFormaVertices = floatArrayOf(...)
```

3. **EducationalContent.kt**
```kotlin
case GeometryType.NOVA_FORMA -> EducationalContent(...)
```

### Customizar Cores

Edite `res/values/colors.xml` ou use `MaterialTheme.colorScheme`.

## Debugging

### Logs ARCore

```bash
adb logcat | grep ARCore
```

### Logs do App

```bash
adb logcat | grep GeoAR
```

### Verificar Plane Detection

```kotlin
Log.d("AR", "Planos detectados: ${sessionManager.getDetectedPlanes().size}")
```

## Testes

### Testes Unitários
```bash
./gradlew test
```

### Testes de Integração
```bash
./gradlew connectedAndroidTest
```

### Testes de UI
```bash
./gradlew assembleDebugAndroidTest
```

## Build e Deploy

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Assinatura

Configure `android/app/build.gradle.kts` com keystore:
```kotlin
signingConfigs {
    create("release") {
        storeFile = file("keystore.jks")
        storePassword = "..."
        keyAlias = "..."
        keyPassword = "..."
    }
}
```

## Referências

- [ARCore Documentation](https://developers.google.com/ar/develop)
- [OpenGL ES Guide](https://developer.android.com/guide/topics/graphics/opengl)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Android Architecture](https://developer.android.com/jetpack/guide)

## Contribuição

Para contribuir:
1. Fork o repositório
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Add nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Licença

MIT License - Veja LICENSE para detalhes.

---

**GeoAR** - Ensinando geometria através de Realidade Aumentada

