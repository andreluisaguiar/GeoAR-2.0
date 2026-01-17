# Como fazer Push do GeoAR para o GitHub

## 1️⃣ Crie o repositório no GitHub

1. Acesse: https://github.com/new
2. Preencha:
   - **Repository name**: `GeoAR`
   - **Description**: `Aplicativo educacional de Geometria em AR com Unity e ARCore`
   - **Visibility**: Escolha entre Public (para compartilhar) ou Private
   - **Add a README**: deixe em branco (você já tem um)
3. Clique em **Create repository**

## 2️⃣ Configure o remote no seu repositório local

Substitua `seu-usuario` pelo seu GitHub username:

```bash
cd /home/andre-aguiar/Documentos/GeoAR

# Adicione o repositório remoto
git remote add origin https://github.com/seu-usuario/GeoAR.git

# Rebatize a branch padrão para main (opcional, mas recomendado)
git branch -M main

# Verifique a configuração
git remote -v
```

## 3️⃣ Faça o push inicial para GitHub

```bash
git push -u origin main
```

### 🔐 Autenticação no GitHub

Na primeira vez, você será solicitado a autenticar. Escolha uma opção:

#### Opção A: HTTPS + GitHub CLI (Recomendado)

```bash
# Instale GitHub CLI (se não tiver)
sudo apt install gh

# Faça login
gh auth login

# Selecione:
# - GitHub.com
# - HTTPS
# - Autentique com seu navegador
```

Depois faça o push:
```bash
git push -u origin main
```

#### Opção B: HTTPS + Personal Access Token

1. Acesse: https://github.com/settings/tokens
2. Clique em **Generate new token**
3. Selecione escopos: `repo` (full control)
4. Copie o token
5. Quando Git pedir senha, use o token

```bash
git push -u origin main
# Username: seu-usuario
# Password: seu-token-aqui
```

#### Opção C: SSH (Avançado)

```bash
# Gere chave SSH (se não tiver)
ssh-keygen -t ed25519 -C "seu-email@example.com"

# Adicione à chave ao ssh-agent
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

# Copie a chave pública
cat ~/.ssh/id_ed25519.pub
```

1. Acesse: https://github.com/settings/ssh
2. Clique em **New SSH key**
3. Cole a chave pública
4. No repositório local, altere o remote:

```bash
git remote set-url origin git@github.com:seu-usuario/GeoAR.git

# Faça o push
git push -u origin main
```

---

## 4️⃣ Verifique o resultado

Após o push bem-sucedido:

```bash
# Verifique as branches remotas
git branch -a

# Você deve ver:
#   master
# * main
#   remotes/origin/main
```

Acesse seu repositório no GitHub para confirmar:
```
https://github.com/seu-usuario/GeoAR
```

---

## 📝 Próximos Commits

Para adicionar mudanças futuras:

```bash
# Faça alterações nos arquivos...

# Verifique o status
git status

# Adicione as mudanças
git add .
# ou específicos:
# git add Assets/GeoAR/Scripts/NovoScript.cs

# Faça o commit com mensagem descritiva
git commit -m "Adiciona nova funcionalidade XYZ"

# Envie para o GitHub
git push origin main
```

---

## 🔄 Fluxo de desenvolvimento recomendado

```bash
# 1. Atualize seu local com o remoto
git pull origin main

# 2. Crie uma branch para nova feature
git checkout -b feature/nova-funcionalidade

# 3. Faça commits
git add .
git commit -m "Descrição da mudança"

# 4. Envie para GitHub
git push origin feature/nova-funcionalidade

# 5. Abra um Pull Request no GitHub
# - Vá para o repositório
# - Clique em "Pull requests"
# - Clique em "New pull request"
# - Selecione sua branch
# - Descreva as mudanças
# - Clique em "Create pull request"

# 6. Após revisar, faça merge na main
git checkout main
git merge feature/nova-funcionalidade
git push origin main
```

---

## ⚠️ Problemas Comuns

### "fatal: Authentication failed"

```bash
# Use GitHub CLI para autenticar
gh auth login
gh auth refresh -h github.com -s admin:public_key,repo,gist
```

### "fatal: The remote end hung up unexpectedly"

O repositório é grande. Tente:

```bash
git config http.postBuffer 157286400
git push -u origin main
```

### "hint: Pulling without specifying how to reconcile divergent branches"

```bash
# Configure estratégia de merge
git config pull.rebase false
git pull origin main
```

---

## 📊 Confirme com:

```bash
# Liste informações do remoto
git remote show origin

# Verifique histórico de commits
git log --oneline -5

# Verifique branches
git branch -a
```

---

**Pronto!** 🎉 Seu GeoAR agora está no GitHub. Compartilhe o link:
```
https://github.com/seu-usuario/GeoAR
```
