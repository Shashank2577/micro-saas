# CareerPath Detailed Specification

## 1. Overview
CareerPath is a career development and progression planning platform. It helps employees understand career trajectories, identify skill gaps, and plan growth paths. Features include career roadmap builder, skill gap analysis, role recommendation engine, learning path suggestions, mentor matching, and promotion readiness assessment.

## 2. Database Schema (PostgreSQL)
All tables must include `tenant_id` for multitenancy.

- **roles**: `id`, `tenant_id`, `title`, `department`, `level`, `description`, `created_at`, `updated_at`
- **skills**: `id`, `tenant_id`, `name`, `category`, `description`, `created_at`, `updated_at`
- **role_skills**: `id`, `tenant_id`, `role_id`, `skill_id`, `required_proficiency`, `is_core`
- **career_paths**: `id`, `tenant_id`, `from_role_id`, `to_role_id`, `description`
- **employees**: `id`, `tenant_id`, `user_id`, `current_role_id`, `manager_id`, `career_goals`, `created_at`, `updated_at`
- **employee_skills**: `id`, `tenant_id`, `employee_id`, `skill_id`, `current_proficiency`
- **mentorship_requests**: `id`, `tenant_id`, `mentee_id`, `mentor_id`, `status`, `goals`
- **development_plans**: `id`, `tenant_id`, `employee_id`, `target_role_id`, `status`, `milestones_json`, `created_at`

## 3. API Endpoints
- `GET /api/v1/roles` - Get all roles
- `GET /api/v1/roles/{id}` - Get role details including required skills
- `GET /api/v1/roadmaps` - Get career roadmap (nodes and edges for React Flow)
- `GET /api/v1/employees/{id}/skills` - Get employee skill inventory
- `POST /api/v1/employees/{id}/skills` - Update employee skill
- `GET /api/v1/employees/{id}/skill-gaps?targetRoleId={roleId}` - Calculate skill gaps
- `POST /api/v1/employees/{id}/recommend-roles` - Generate role recommendations (LiteLLM)
- `POST /api/v1/employees/{id}/learning-paths` - Generate learning paths (LiteLLM)
- `POST /api/v1/mentors/match` - Recommend mentors based on employee goals (LiteLLM)
- `POST /api/v1/employees/{id}/assess-promotion` - Assess promotion readiness (LiteLLM)
- `POST /api/v1/managers/{id}/coaching-guidance?employeeId={empId}` - Get coaching guidance (LiteLLM)

## 4. Frontend Components
- `RoadmapViewer`: Uses React Flow to visualize roles and transitions
- `SkillInventory`: Table/List to view and update employee skills
- `SkillGapAnalyzer`: Comparison view between current skills and target role required skills
- `RoleRecommendations`: Display AI-suggested next career moves
- `LearningPaths`: Display AI-suggested courses and certifications
- `MentorMatch`: Interface to find and request mentors
- `PromotionReadiness`: Display promotion readiness score and feedback
- `ManagerDashboard`: View team members and get coaching guidance

## 5. AI Integrations (LiteLLM)
- **Role Recommendation**: Suggest target roles based on employee's current skills and interests.
- **Learning Path**: Suggest specific courses/projects to bridge skill gaps.
- **Mentor Matching**: Match mentees with mentors based on skills and goals.
- **Promotion Readiness**: Assess if an employee is ready for the next level based on skill gaps.
- **Coaching Guidance**: Generate talking points for managers for career discussions.

## 6. Acceptance Criteria Tests
- Test that employee can view career roadmap.
- Test skill gap calculation accuracy.
- Test role recommendations API call to LiteLLM.
- Test multitenancy filters in all JPA queries.
