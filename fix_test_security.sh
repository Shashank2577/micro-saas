#!/bin/bash
for f in onboardflow/backend/src/test/java/com/microsaas/onboardflow/controller/*Test.java; do
  sed -i 's/@WebMvcTest(controllers = /@WebMvcTest(controllers = /g' "$f"
  sed -i 's/import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;/import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;\nimport org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;\nimport org.springframework.test.web.servlet.MockMvc;\nimport org.springframework.security.test.context.support.WithMockUser;\nimport static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;/g' "$f"
  sed -i 's/public void testGetAll() throws Exception {/\@WithMockUser\n    public void testGetAll() throws Exception {/g' "$f"
  sed -i 's/public void testCreate() throws Exception {/\@WithMockUser\n    public void testCreate() throws Exception {/g' "$f"
  sed -i 's/mockMvc.perform(post("/mockMvc.perform(post("/g' "$f"
  sed -i 's/mockMvc.perform(post(..api.v1.onboardflow.{api_path}..)/mockMvc.perform(post("\/api\/v1\/onboardflow\/{api_path}").with(csrf())/g' "$f"
done
