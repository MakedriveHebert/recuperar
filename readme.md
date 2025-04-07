# Recuperação de Desastre PITR (Point-in-Time Recovery) no Firebase Firestore

Este documento descreve o processo de recuperação de dados excluídos acidentalmente do Firestore utilizando o recurso PITR (Point-in-Time Recovery).

## Pré-requisitos

- Projeto Firebase com Firestore ativado
- PITR habilitado no Firestore (entre 7 e 30 dias de retenção)
- Um bucket do Google Cloud Storage para armazenar a exportação
- Permissões adequadas para acessar o Firestore e o Cloud Storage

## Passo a Passo da Recuperação

### 1. Acessar o Google Cloud Shell

Acesse o [Google Cloud Console](https://console.cloud.google.com/) e abra o Cloud Shell para executar os comandos.

### 2. Criar um bucket para a exportação (se ainda não existir)

```bash
gsutil mb -p [PROJETO_ID] gs://[NOME_DO_BUCKET]

exemplo de criação de bucket
gsutil mb -p makeduka gs://makeduka-backup

### 3. Exportar dados do ponto específico no tempo

gcloud firestore export gs://[NOME_DO_BUCKET]/[PASTA_DESTINO] \
    --snapshot-time="[DATA_HORA_Z]" \
    --collection-ids=[COLEÇÕES]

exemplo usado

gcloud firestore export gs://makeduka-backup/pitr-recovery-20250402 \
    --snapshot-time="2025-04-02T18:47:00Z" \
    --collection-ids=usuarios

onde 
snapshot-time: Momento exato antes da exclusão acidental
collection-ids: Lista de coleções a serem exportadas (separadas por vírgula)

### 4.Verificar status da Exportação
gcloud firestore operations list

A resposta deve mostrar o estado atual da operação (PROCESSING → SUCCESSFUL).

### 5. Importar os dados recuperados de volta ao Firestore

gcloud firestore import gs://[NOME_DO_BUCKET]/[PASTA_DESTINO]

exemplo 

gcloud firestore import gs://makeduka-backup/pitr-recovery-20250402