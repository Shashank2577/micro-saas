# EducationOS Detailed Specification

## 1. Overview
EducationOS is an AI learning platform builder. It generates personalized learning paths from curriculum goals and learner background, creates assessments and quizzes from content automatically, tracks learner progress with mastery detection, and adapts content difficulty.

## 2. Database Schema

```sql
CREATE TABLE courses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE modules (
    id UUID PRIMARY KEY,
    course_id UUID NOT NULL REFERENCES courses(id),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    difficulty_level VARCHAR(50) NOT NULL,
    order_index INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE learner_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    background_info TEXT,
    learning_style VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE quizzes (
    id UUID PRIMARY KEY,
    module_id UUID NOT NULL REFERENCES modules(id),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE questions (
    id UUID PRIMARY KEY,
    quiz_id UUID NOT NULL REFERENCES quizzes(id),
    tenant_id UUID NOT NULL,
    question_text TEXT NOT NULL,
    options JSONB NOT NULL,
    correct_option_index INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE progress_records (
    id UUID PRIMARY KEY,
    learner_id UUID NOT NULL REFERENCES learner_profiles(id),
    module_id UUID NOT NULL REFERENCES modules(id),
    tenant_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL, -- NOT_STARTED, IN_PROGRESS, COMPLETED
    score DECIMAL(5,2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## 3. REST Endpoints

### 3.1 Courses
- `GET /api/courses` - List courses (filtered by tenant)
- `POST /api/courses` - Create a course
- `GET /api/courses/{id}` - Get a course
- `PUT /api/courses/{id}` - Update a course
- `DELETE /api/courses/{id}` - Delete a course

### 3.2 Modules
- `GET /api/courses/{courseId}/modules` - List modules
- `POST /api/courses/{courseId}/modules` - Create a module
- `PUT /api/modules/{id}` - Update a module

### 3.3 Learner Profiles
- `GET /api/learners/me` - Get current learner profile
- `POST /api/learners/me` - Create/update learner profile

### 3.4 Quizzes & AI Generation
- `POST /api/modules/{moduleId}/quizzes/generate` - Generate quiz from module content via AI
- `GET /api/modules/{moduleId}/quizzes` - List quizzes for module
- `POST /api/quizzes/{quizId}/submit` - Submit quiz and calculate score

## 4. Services

- `CourseService`: Manages courses and modules.
- `LearnerService`: Manages learner profiles and progress tracking.
- `QuizGenerationService`: Calls LiteLLM to generate quiz questions based on module content.

## 5. Next.js Frontend

### Pages
- `/courses` - Course list dashboard
- `/courses/[id]` - Course details
- `/courses/[id]/modules/[moduleId]` - Module learning view
- `/learners/profile` - Learner profile setup

### Components
- `CourseCard`: Displays course summary
- `ModuleList`: Displays modules in a course
- `QuizView`: Renders a quiz and handles submission
- `ProgressChart`: Displays learner mastery

## 6. Integrations
- Emits events: `course.created`, `quiz.completed`
- Consumes events: `user.created` (to init learner profile)

## 7. Error Handling
- `404 Not Found` for missing resources.
- `403 Forbidden` for tenant mismatch.
- Fallback for AI failure: Return error message instructing user to try again.

